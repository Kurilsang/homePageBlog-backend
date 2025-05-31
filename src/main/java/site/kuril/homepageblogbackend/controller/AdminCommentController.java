package site.kuril.homepageblogbackend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.kuril.homepageblogbackend.common.Result;
import site.kuril.homepageblogbackend.service.CommentService;
import site.kuril.homepageblogbackend.vo.CommentVO;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员评论控制器
 * 
 * @Author Kuril
 */
@Api(tags = "管理员评论接口", description = "管理员评论管理接口")
@RestController
@RequestMapping("/api/admin/comments")
public class AdminCommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 获取所有评论（管理员）
     */
    @ApiOperation(value = "获取所有评论", notes = "管理员获取所有评论，支持分页和筛选")
    @GetMapping
    public Result<Map<String, Object>> getCommentList(
            @ApiParam(value = "当前页码", defaultValue = "1") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页条数", defaultValue = "10") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(value = "评论状态", defaultValue = "all") @RequestParam(defaultValue = "all") String status,
            @ApiParam(value = "搜索关键词") @RequestParam(required = false) String search,
            @ApiParam(value = "用户类型", defaultValue = "all") @RequestParam(defaultValue = "all") String userType) {

        IPage<CommentVO> pageResult = commentService.getCommentList(page, pageSize, status, search, userType);
        
        Map<String, Object> result = new HashMap<>();
        result.put("total", pageResult.getTotal());
        result.put("comments", pageResult.getRecords());
        
        return Result.success(result);
    }

    /**
     * 更新评论状态
     */
    @ApiOperation(value = "更新评论状态", notes = "管理员审核评论（通过/拒绝）")
    @PatchMapping("/{id}/status")
    public Result<Boolean> updateCommentStatus(
            @ApiParam(value = "评论ID", required = true) @PathVariable Integer id,
            @ApiParam(value = "评论状态", required = true, example = "approved/rejected") @RequestParam String status) {
        
        boolean success = commentService.updateCommentStatus(id, status);
        return Result.success(success);
    }

    /**
     * 删除评论
     */
    @ApiOperation(value = "删除评论", notes = "管理员删除评论")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteComment(
            @ApiParam(value = "评论ID", required = true) @PathVariable Integer id) {
        boolean success = commentService.deleteComment(id);
        return Result.success(success);
    }
} 