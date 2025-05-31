package site.kuril.homepageblogbackend.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 个人资料视图对象
 * 
 * @Author Kuril
 */
@Data
@ApiModel(description = "个人资料信息")
public class ProfileVO {
    /**
     * 头像URL
     */
    @ApiModelProperty(value = "头像URL")
    private String avatar;

    /**
     * 标题/昵称
     */
    @ApiModelProperty(value = "标题/昵称")
    private String title;

    /**
     * 个人描述
     */
    @ApiModelProperty(value = "个人描述")
    private String description;
}