package site.kuril.homepageblogbackend.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * 文章列表视图对象
 * 
 * @Author Kuril
 */
@Data
@ApiModel(description = "文章列表项信息")
public class ArticleListVO {
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
     * 是否置顶
     */
    @ApiModelProperty(value = "是否置顶")
    private Boolean isPinned;
} 