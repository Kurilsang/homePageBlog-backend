package site.kuril.homepageblogbackend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章关键词关联实体类
 */
@Data
@TableName("article_keyword")
public class ArticleKeyword {
    /**
     * 关联ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 文章ID
     */
    private Integer articleId;

    /**
     * 关键词
     */
    private String keyword;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
} 