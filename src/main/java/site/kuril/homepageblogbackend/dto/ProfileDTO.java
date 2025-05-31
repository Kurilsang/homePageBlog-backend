package site.kuril.homepageblogbackend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 个人资料数据传输对象
 * 
 * @Author Kuril
 */
@Data
@ApiModel(description = "个人资料更新参数")
public class ProfileDTO {
    /**
     * 头像URL
     */
    @ApiModelProperty(value = "头像URL", required = false)
    private String avatar;

    /**
     * 标题/昵称
     */
    @ApiModelProperty(value = "标题/昵称", required = false)
    private String title;

    /**
     * 个人描述
     */
    @ApiModelProperty(value = "个人描述", required = false)
    private String description;
} 