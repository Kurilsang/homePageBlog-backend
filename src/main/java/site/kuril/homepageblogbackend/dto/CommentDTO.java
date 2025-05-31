package site.kuril.homepageblogbackend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 评论数据传输对象
 * 
 * @Author Kuril
 */
@Data
@ApiModel(description = "评论创建参数")
public class CommentDTO {
    /**
     * 评论ID
     */
    @ApiModelProperty(value = "评论ID（更新时使用）", required = false)
    private Integer id;

    /**
     * 文章ID
     */
    @ApiModelProperty(value = "文章ID", required = true)
    @NotNull(message = "文章ID不能为空")
    private Integer articleId;

    /**
     * 父评论ID
     */
    @ApiModelProperty(value = "父评论ID（回复评论时使用）", required = false)
    private Integer parentId;

    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容", required = true)
    @NotBlank(message = "评论内容不能为空")
    private String content;

    /**
     * 评论作者（未登录用户使用）
     */
    @ApiModelProperty(value = "评论作者（未登录用户必填）", required = false)
    private String author;

    /**
     * 头像URL（未登录用户使用）
     */
    @ApiModelProperty(value = "头像URL（未登录用户可选）", required = false)
    private String avatar;
}