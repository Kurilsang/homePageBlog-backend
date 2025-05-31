package site.kuril.homepageblogbackend.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理员视图对象
 * 
 * @Author Kuril
 */
@Data
@ApiModel(description = "管理员信息")
public class AdminVO {
    /**
     * 管理员ID
     */
    @ApiModelProperty(value = "管理员ID")
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
     * 最后登录时间
     */
    @ApiModelProperty(value = "最后登录时间")
    private LocalDateTime lastLogin;
} 