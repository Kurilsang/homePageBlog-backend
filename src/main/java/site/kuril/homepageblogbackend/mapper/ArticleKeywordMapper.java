package site.kuril.homepageblogbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import site.kuril.homepageblogbackend.model.ArticleKeyword;

import java.util.List;

/**
 * 文章关键词关联Mapper接口
 */
@Mapper
public interface ArticleKeywordMapper extends BaseMapper<ArticleKeyword> {
    /**
     * 根据文章ID查询关键词列表
     *
     * @param articleId 文章ID
     * @return 关键词列表
     */
    List<String> selectKeywordsByArticleId(@Param("articleId") Integer articleId);
} 