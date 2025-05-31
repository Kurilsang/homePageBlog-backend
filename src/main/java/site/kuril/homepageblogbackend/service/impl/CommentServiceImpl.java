package site.kuril.homepageblogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.kuril.homepageblogbackend.dto.CommentDTO;
import site.kuril.homepageblogbackend.mapper.ArticleMapper;
import site.kuril.homepageblogbackend.mapper.CommentMapper;
import site.kuril.homepageblogbackend.mapper.UserMapper;
import site.kuril.homepageblogbackend.model.Article;
import site.kuril.homepageblogbackend.model.Comment;
import site.kuril.homepageblogbackend.model.User;
import site.kuril.homepageblogbackend.service.CommentService;
import site.kuril.homepageblogbackend.vo.CommentVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论服务实现类
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<CommentVO> getArticleComments(Integer articleId, boolean isAdmin) {
        // 非管理员只能查看已审核评论
        String status = isAdmin ? null : "approved";
        return commentMapper.selectArticleComments(articleId, status);
    }

    @Override
    public IPage<CommentVO> getCommentList(Integer page, Integer pageSize, String status, String search, String userType) {
        Page<Comment> pageParam = new Page<>(page, pageSize);
        return commentMapper.selectCommentList(pageParam, status, search, userType);
    }

    @Override
    public IPage<CommentVO> getUserComments(Integer page, Integer pageSize, Integer userId) {
        Page<Comment> pageParam = new Page<>(page, pageSize);
        return commentMapper.selectUserComments(pageParam, userId);
    }

    @Override
    @Transactional
    public Integer addComment(CommentDTO commentDTO, Integer userId, String ipAddress, String userAgent) {
        // 检查文章是否存在
        Article article = articleMapper.selectById(commentDTO.getArticleId());
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }

        // 如果有父评论ID，检查父评论是否存在
        if (commentDTO.getParentId() != null) {
            Comment parentComment = getById(commentDTO.getParentId());
            if (parentComment == null) {
                throw new RuntimeException("回复的评论不存在");
            }
        }

        // 创建评论
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);

        // 设置用户信息
        if (userId != null) {
            // 已登录用户
            User user = userMapper.selectById(userId);
            if (user == null) {
                throw new RuntimeException("用户不存在");
            }
            comment.setUserId(userId);
            comment.setAuthor(user.getNickname());
            comment.setAvatar(user.getAvatar());
        } else {
            // 未登录用户，使用提供的作者信息
            if (commentDTO.getAuthor() == null || commentDTO.getAuthor().trim().isEmpty()) {
                throw new RuntimeException("未登录用户必须提供作者名称");
            }
        }

        // 设置其他信息
        comment.setStatus("pending"); // 默认待审核
        comment.setIpAddress(ipAddress);
        comment.setUserAgent(userAgent);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());

        // 保存评论
        save(comment);

        return comment.getId();
    }

    @Override
    public boolean updateCommentStatus(Integer id, String status) {
        // 检查评论是否存在
        Comment comment = getById(id);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }

        // 检查状态是否有效
        if (!status.equals("approved") && !status.equals("rejected")) {
            throw new RuntimeException("无效的评论状态");
        }

        // 更新状态
        comment.setStatus(status);
        comment.setUpdatedAt(LocalDateTime.now());
        return updateById(comment);
    }

    @Override
    @Transactional
    public boolean deleteComment(Integer id) {
        // 检查评论是否存在
        if (!getBaseMapper().exists(new LambdaQueryWrapper<Comment>().eq(Comment::getId, id))) {
            throw new RuntimeException("评论不存在");
        }

        // 删除以该评论为父评论的回复
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId, id);
        remove(queryWrapper);

        // 删除评论
        return removeById(id);
    }
} 