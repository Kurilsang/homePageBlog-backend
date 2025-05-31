package site.kuril.homepageblogbackend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 密码更新数据传输对象
 * 
 * @Author Kuril
 */
@Data
@ApiModel(description = "密码更新参数")
public class PasswordUpdateDTO {
    /**
     * 当前密码
     */
    @ApiModelProperty(value = "当前密码", required = true)
    @NotBlank(message = "当前密码不能为空")
    private String currentPassword;

    /**
     * 新密码
     */
    @ApiModelProperty(value = "新密码", required = true)
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度为6-20个字符")
    private String newPassword;
} 