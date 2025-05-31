package site.kuril.homepageblogbackend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.kuril.homepageblogbackend.common.Result;
import site.kuril.homepageblogbackend.dto.CommentDTO;
import site.kuril.homepageblogbackend.service.AuthService;
import site.kuril.homepageblogbackend.service.CommentService;
import site.kuril.homepageblogbackend.vo.CommentVO;
import site.kuril.homepageblogbackend.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论控制器
 * 
 * @Author Kuril
 */
@Api(tags = "评论接口", description = "文章评论的查看和添加")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private AuthService authService;

    /**
     * 获取文章评论
     */
    @ApiOperation(value = "获取文章评论", notes = "获取指定文章的评论列表")
    @GetMapping("/api/articles/{id}/comments")
    public Result<List<CommentVO>> getArticleComments(
            @ApiParam(value = "文章ID", required = true) @PathVariable("id") Integer articleId) {
        // 判断是否为管理员
        boolean isAdmin = false;
        try {
            // 尝试获取管理员信息，如果成功则为管理员
            authService.getCurrentAdmin();
            isAdmin = true;
        } catch (Exception e) {
            // 不是管理员，忽略异常
        }

        List<CommentVO> comments = commentService.getArticleComments(articleId, isAdmin);
        return Result.success(comments);
    }

    /**
     * 添加评论
     */
    @ApiOperation(value = "添加评论", notes = "添加文章评论")
    @PostMapping("/api/comments")
    public Result<Map<String, Object>> addComment(
            @ApiParam(value = "评论信息", required = true) @Valid @RequestBody CommentDTO commentDTO,
            HttpServletRequest request) {
        
        // 尝试获取当前登录用户
        Integer userId = null;
        try {
            UserVO currentUser = authService.getCurrentUser();
            if (currentUser != null) {
                userId = currentUser.getId();
            }
        } catch (Exception e) {
            // 未登录用户，忽略异常
        }

        // 获取IP地址和浏览器信息
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        // 添加评论
        Integer id = commentService.addComment(commentDTO, userId, ipAddress, userAgent);

        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("success", true);

        return Result.success(result);
    }
} 