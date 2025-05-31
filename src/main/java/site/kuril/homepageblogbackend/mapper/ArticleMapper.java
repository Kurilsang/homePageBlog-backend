package site.kuril.homepageblogbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import site.kuril.homepageblogbackend.model.Article;
import site.kuril.homepageblogbackend.vo.ArticleListVO;

/**
 * 文章Mapper接口
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 分页查询文章列表
     *
     * @param page 分页对象
     * @param search 搜索关键词
     * @param tag 标签筛选
     * @param pinned 是否只获取置顶文章
     * @return 文章列表VO分页对象
     */
    IPage<ArticleListVO> selectArticleList(Page<Article> page, @Param("search") String search, 
                                          @Param("tag") String tag, @Param("pinned") Boolean pinned);
} 