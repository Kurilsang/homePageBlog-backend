package site.kuril.homepageblogbackend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

/**
 * 文章数据传输对象
 * 
 * @Author Kuril
 */
@Data
@ApiModel(description = "文章创建/更新参数")
public class ArticleDTO {
    /**
     * 文章ID（更新时使用）
     */
    @ApiModelProperty(value = "文章ID（更新时使用）", required = false)
    private Integer id;

    /**
     * 文章标题
     */
    @ApiModelProperty(value = "文章标题", required = true)
    @NotBlank(message = "文章标题不能为空")
    private String title;

    /**
     * 文章摘要
     */
    @ApiModelProperty(value = "文章摘要", required = true)
    @NotBlank(message = "文章摘要不能为空")
    private String summary;

    /**
     * 文章内容
     */
    @ApiModelProperty(value = "文章内容", required = true)
    @NotBlank(message = "文章内容不能为空")
    private String content;

    /**
     * 封面图片URL
     */
    @ApiModelProperty(value = "封面图片URL", required = false)
    private String coverImage;

    /**
     * 发布日期，不传则使用当前日期
     */
    @ApiModelProperty(value = "发布日期", required = false, example = "2023-05-01")
    private LocalDate date;

    /**
     * 文章标签
     */
    @ApiModelProperty(value = "文章标签", required = false)
    private String tag;

    /**
     * 关键词数组
     */
    @ApiModelProperty(value = "关键词数组", required = false)
    private List<String> keywords;

    /**
     * 是否置顶
     */
    @ApiModelProperty(value = "是否置顶", required = false)
    private Boolean isPinned;
} 