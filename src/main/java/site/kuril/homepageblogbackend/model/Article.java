package site.kuril.homepageblogbackend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章实体类
 */
@Data
@TableName("article")
public class Article {
    /**
     * 文章ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 文章内容（HTML格式）
     */
    private String content;

    /**
     * 封面图片URL
     */
    private String coverImage;

    /**
     * 发布日期
     */
    private LocalDate publishDate;

    /**
     * 文章标签
     */
    private String tag;

    /**
     * 是否置顶
     */
    private Boolean isPinned;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 关键词列表（非数据库字段）
     */
    @TableField(exist = false)
    private List<String> keywords;
} 