package site.kuril.homepageblogbackend.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 文章视图对象
 * 
 * @Author Kuril
 */
@Data
@ApiModel(description = "文章详情信息")
public class ArticleVO {
    /**
     * 文章ID
     */
    @ApiModelProperty(value = "文章ID")
    private Integer id;

    /**
     * 文章标题
     */
    @ApiModelProperty(value = "文章标题")
    private String title;

    /**
     * 文章摘要
     */
    @ApiModelProperty(value = "文章摘要")
    private String summary;

    /**
     * 文章内容（HTML格式）
     */
    @ApiModelProperty(value = "文章内容（HTML格式）")
    private String content;

    /**
     * 封面图片URL
     */
    @ApiModelProperty(value = "封面图片URL")
    private String coverImage;

    /**
     * 发布日期
     */
    @ApiModelProperty(value = "发布日期", example = "2023-05-01")
    private LocalDate date;

    /**
     * 文章标签
     */
    @ApiModelProperty(value = "文章标签")
    private String tag;

    /**
     * 关键词数组
     */
    @ApiModelProperty(value = "关键词数组")
    private List<String> keywords;

    /**
     * 是否置顶
     */
    @ApiModelProperty(value = "是否置顶")
    private Boolean isPinned;

    /**
     * 浏览次数
     */
    @ApiModelProperty(value = "浏览次数")
    private Integer viewCount;
} 