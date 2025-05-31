package site.kuril.homepageblogbackend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户注册数据传输对象
 * 
 * @Author Kuril
 */
@Data
@ApiModel(description = "用户注册参数")
public class UserRegisterDTO {
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = true, example = "user123")
    @NotBlank(message = "用户名不能为空")
    @Size(min = 5, max = 20, message = "用户名长度为5-20个字符")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度为6-20个字符")
    private String password;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称", required = true)
    @NotBlank(message = "昵称不能为空")
    @Size(max = 50, message = "昵称最长50个字符")
    private String nickname;

    /**
     * 电子邮件
     */
    @ApiModelProperty(value = "电子邮件", required = false, example = "user@example.com")
    @Email(message = "邮箱格式不正确")
    private String email;
} 