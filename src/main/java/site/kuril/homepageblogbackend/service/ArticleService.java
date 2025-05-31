package site.kuril.homepageblogbackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import site.kuril.homepageblogbackend.dto.ArticleDTO;
import site.kuril.homepageblogbackend.model.Article;
import site.kuril.homepageblogbackend.vo.ArticleListVO;
import site.kuril.homepageblogbackend.vo.ArticleVO;

/**
 * 文章服务接口
 */
public interface ArticleService extends IService<Article> {
    /**
     * 分页获取文章列表
     *
     * @param page 当前页码
     * @param pageSize 每页条数
     * @param search 搜索关键词
     * @param tag 标签筛选
     * @param pinned 是否只获取置顶文章
     * @return 文章列表分页对象
     */
    IPage<ArticleListVO> getArticleList(Integer page, Integer pageSize, String search, String tag, Boolean pinned);

    /**
     * 获取文章详情
     *
     * @param id 文章ID
     * @return 文章详情
     */
    ArticleVO getArticleDetail(Integer id);

    /**
     * 创建文章
     *
     * @param articleDTO 文章信息
     * @return 新创建的文章ID
     */
    Integer createArticle(ArticleDTO articleDTO);

    /**
     * 更新文章
     *
     * @param id 文章ID
     * @param articleDTO 文章信息
     * @return 是否成功
     */
    boolean updateArticle(Integer id, ArticleDTO articleDTO);

    /**
     * 切换文章置顶状态
     *
     * @param id 文章ID
     * @param isPinned 是否置顶
     * @return 是否成功
     */
    boolean toggleArticlePin(Integer id, Boolean isPinned);

    /**
     * 删除文章
     *
     * @param id 文章ID
     * @return 是否成功
     */
    boolean deleteArticle(Integer id);

    /**
     * 增加文章浏览次数
     *
     * @param id 文章ID
     */
    void increaseViewCount(Integer id);
} 