package site.kuril.homepageblogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.kuril.homepageblogbackend.dto.ArticleDTO;
import site.kuril.homepageblogbackend.mapper.ArticleKeywordMapper;
import site.kuril.homepageblogbackend.mapper.ArticleMapper;
import site.kuril.homepageblogbackend.mapper.CommentMapper;
import site.kuril.homepageblogbackend.model.Article;
import site.kuril.homepageblogbackend.model.ArticleKeyword;
import site.kuril.homepageblogbackend.service.ArticleService;
import site.kuril.homepageblogbackend.vo.ArticleListVO;
import site.kuril.homepageblogbackend.vo.ArticleVO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章服务实现类
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleKeywordMapper articleKeywordMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public IPage<ArticleListVO> getArticleList(Integer page, Integer pageSize, String search, String tag, Boolean pinned) {
        Page<Article> pageParam = new Page<>(page, pageSize);
        return articleMapper.selectArticleList(pageParam, search, tag, pinned);
    }

    @Override
    public ArticleVO getArticleDetail(Integer id) {
        // 查询文章
        Article article = getById(id);
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }

        // 查询关键词
        List<String> keywords = articleKeywordMapper.selectKeywordsByArticleId(id);

        // 转换为VO
        ArticleVO articleVO = new ArticleVO();
        BeanUtils.copyProperties(article, articleVO);
        articleVO.setDate(article.getPublishDate());
        articleVO.setKeywords(keywords);

        return articleVO;
    }

    @Override
    @Transactional
    public Integer createArticle(ArticleDTO articleDTO) {
        // 创建文章
        Article article = new Article();
        BeanUtils.copyProperties(articleDTO, article);
        
        // 设置默认值
        if (article.getPublishDate() == null) {
            article.setPublishDate(LocalDate.now());
        }
        if (article.getIsPinned() == null) {
            article.setIsPinned(false);
        }
        article.setViewCount(0);
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        // 保存文章
        save(article);

        // 保存关键词
        saveArticleKeywords(article.getId(), articleDTO.getKeywords());

        return article.getId();
    }

    @Override
    @Transactional
    public boolean updateArticle(Integer id, ArticleDTO articleDTO) {
        // 检查文章是否存在
        Article article = getById(id);
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }

        // 更新文章
        BeanUtils.copyProperties(articleDTO, article);
        article.setUpdatedAt(LocalDateTime.now());
        updateById(article);

        // 更新关键词
        // 先删除原有关键词
        LambdaQueryWrapper<ArticleKeyword> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleKeyword::getArticleId, id);
        articleKeywordMapper.delete(queryWrapper);

        // 保存新关键词
        saveArticleKeywords(id, articleDTO.getKeywords());

        return true;
    }

    @Override
    public boolean toggleArticlePin(Integer id, Boolean isPinned) {
        // 检查文章是否存在
        Article article = getById(id);
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }

        // 更新置顶状态
        article.setIsPinned(isPinned);
        article.setUpdatedAt(LocalDateTime.now());
        return updateById(article);
    }

    @Override
    @Transactional
    public boolean deleteArticle(Integer id) {
        // 检查文章是否存在
        if (!getBaseMapper().exists(new LambdaQueryWrapper<Article>().eq(Article::getId, id))) {
            throw new RuntimeException("文章不存在");
        }

        // 删除文章关键词关联
        LambdaQueryWrapper<ArticleKeyword> keywordQueryWrapper = new LambdaQueryWrapper<>();
        keywordQueryWrapper.eq(ArticleKeyword::getArticleId, id);
        articleKeywordMapper.delete(keywordQueryWrapper);

        // 删除文章
        return removeById(id);
    }

    @Override
    public void increaseViewCount(Integer id) {
        // 增加浏览次数
        Article article = getById(id);
        if (article != null) {
            article.setViewCount(article.getViewCount() + 1);
            updateById(article);
        }
    }

    /**
     * 保存文章关键词
     *
     * @param articleId 文章ID
     * @param keywords 关键词列表
     */
    private void saveArticleKeywords(Integer articleId, List<String> keywords) {
        if (keywords != null && !keywords.isEmpty()) {
            for (String keyword : keywords) {
                ArticleKeyword articleKeyword = new ArticleKeyword();
                articleKeyword.setArticleId(articleId);
                articleKeyword.setKeyword(keyword);
                articleKeyword.setCreatedAt(LocalDateTime.now());
                articleKeywordMapper.insert(articleKeyword);
            }
        }
    }
}