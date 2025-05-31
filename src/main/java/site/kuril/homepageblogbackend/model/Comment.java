package site.kuril.homepageblogbackend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评论实体类
 */
@Data
@TableName("comment")
public class Comment {
    /**
     * 评论ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 文章ID
     */
    private Integer articleId;

    /**
     * 文章标题（非数据库字段）
     */
    @TableField(exist = false)
    private String articleTitle;

    /**
     * 父评论ID，用于回复评论
     */
    private Integer parentId;

    /**
     * 父评论作者（非数据库字段）
     */
    @TableField(exist = false)
    private String parentAuthor;

    /**
     * 关联用户ID，已登录用户
     */
    private Integer userId;

    /**
     * 评论作者名称
     */
    private String author;

    /**
     * 作者头像URL
     */
    private String avatar;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论状态
     */
    private String status;

    /**
     * 评论者IP地址
     */
    private String ipAddress;

    /**
     * 评论者浏览器信息
     */
    private String userAgent;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 是否为注册用户（非数据库字段）
     */
    @TableField(exist = false)
    private Boolean isRegistered;
} 