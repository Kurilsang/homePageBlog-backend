package site.kuril.homepageblogbackend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

/**
 * 用户资料更新数据传输对象
 * 
 * @Author Kuril
 */
@Data
@ApiModel(description = "用户资料更新参数")
public class UserProfileUpdateDTO {
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称", required = false)
    @Size(max = 50, message = "昵称最长50个字符")
    private String nickname;

    /**
     * 电子邮件
     */
    @ApiModelProperty(value = "电子邮件", required = false, example = "user@example.com")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 头像URL
     */
    @ApiModelProperty(value = "头像URL", required = false)
    private String avatar;
} 