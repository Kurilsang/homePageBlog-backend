package site.kuril.homepageblogbackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import site.kuril.homepageblogbackend.dto.CommentDTO;
import site.kuril.homepageblogbackend.model.Comment;
import site.kuril.homepageblogbackend.vo.CommentVO;

import java.util.List;

/**
 * 评论服务接口
 */
public interface CommentService extends IService<Comment> {
    /**
     * 获取文章评论列表
     *
     * @param articleId 文章ID
     * @param isAdmin 是否为管理员（管理员可查看所有评论，非管理员只能查看已审核评论）
     * @return 评论列表
     */
    List<CommentVO> getArticleComments(Integer articleId, boolean isAdmin);

    /**
     * 分页获取所有评论（管理员使用）
     *
     * @param page 当前页码
     * @param pageSize 每页条数
     * @param status 评论状态筛选
     * @param search 搜索关键词
     * @param userType 用户类型筛选
     * @return 评论分页对象
     */
    IPage<CommentVO> getCommentList(Integer page, Integer pageSize, String status, String search, String userType);

    /**
     * 分页获取用户评论列表
     *
     * @param page 当前页码
     * @param pageSize 每页条数
     * @param userId 用户ID
     * @return 评论分页对象
     */
    IPage<CommentVO> getUserComments(Integer page, Integer pageSize, Integer userId);

    /**
     * 添加评论
     *
     * @param commentDTO 评论信息
     * @param userId 当前登录用户ID（未登录为null）
     * @param ipAddress 评论者IP地址
     * @param userAgent 评论者浏览器信息
     * @return 新创建的评论ID
     */
    Integer addComment(CommentDTO commentDTO, Integer userId, String ipAddress, String userAgent);

    /**
     * 更新评论状态
     *
     * @param id 评论ID
     * @param status 新状态
     * @return 是否成功
     */
    boolean updateCommentStatus(Integer id, String status);

    /**
     * 删除评论
     *
     * @param id 评论ID
     * @return 是否成功
     */
    boolean deleteComment(Integer id);
} 