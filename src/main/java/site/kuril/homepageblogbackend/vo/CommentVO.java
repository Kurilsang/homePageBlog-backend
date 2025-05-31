package site.kuril.homepageblogbackend.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评论视图对象
 * 
 * @Author Kuril
 */
@Data
@ApiModel(description = "评论信息")
public class CommentVO {
    /**
     * 评论ID
     */
    @ApiModelProperty(value = "评论ID")
    private Integer id;

    /**
     * 文章ID
     */
    @ApiModelProperty(value = "文章ID")
    private Integer articleId;

    /**
     * 文章标题（管理员查看评论列表时使用）
     */
    @ApiModelProperty(value = "文章标题")
    private String articleTitle;

    /**
     * 父评论ID
     */
    @ApiModelProperty(value = "父评论ID")
    private Integer parentId;

    /**
     * 父评论作者
     */
    @ApiModelProperty(value = "父评论作者")
    private String parentAuthor;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    /**
     * 评论作者
     */
    @ApiModelProperty(value = "评论作者")
    private String author;

    /**
     * 头像URL
     */
    @ApiModelProperty(value = "头像URL")
    private String avatar;

    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容")
    private String content;

    /**
     * 评论日期
     */
    @ApiModelProperty(value = "评论日期")
    private LocalDateTime date;

    /**
     * 评论状态
     */
    @ApiModelProperty(value = "评论状态", example = "pending/approved/rejected")
    private String status;

    /**
     * 是否为注册用户
     */
    @ApiModelProperty(value = "是否为注册用户")
    private Boolean isUser;
} 