package site.kuril.homepageblogbackend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统设置实体类
 */
@Data
@TableName("setting")
public class Setting {
    /**
     * 设置ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 设置键名
     */
    private String settingKey;

    /**
     * 设置值
     */
    private String settingValue;

    /**
     * 设置描述
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