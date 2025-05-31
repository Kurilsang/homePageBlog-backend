package site.kuril.homepageblogbackend.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户视图对象
 * 
 * @Author Kuril
 */
@Data
@ApiModel(description = "用户信息")
public class UserVO {
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Integer id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickname;

    /**
     * 电子邮件
     */
    @ApiModelProperty(value = "电子邮件")
    private String email;

    /**
     * 用户头像URL
     */
    @ApiModelProperty(value = "用户头像URL")
    private String avatar;

    /**
     * 用户状态
     */
    @ApiModelProperty(value = "用户状态", example = "active/inactive/banned")
    private String status;

    /**
     * 最后登录时间
     */
    @ApiModelProperty(value = "最后登录时间")
    private LocalDateTime lastLogin;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    /**
     * 评论数量（管理员查看用户列表时使用）
     */
    @ApiModelProperty(value = "评论数量")
    private Integer commentCount;
} 