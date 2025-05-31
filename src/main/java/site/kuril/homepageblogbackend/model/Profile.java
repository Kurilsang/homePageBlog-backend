package site.kuril.homepageblogbackend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 个人资料实体类
 */
@Data
@TableName("profile")
public class Profile {
    /**
     * 资料ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 标题/昵称
     */
    private String title;

    /**
     * 个人描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
} 