package site.kuril.homepageblogbackend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.kuril.homepageblogbackend.common.Result;
import site.kuril.homepageblogbackend.service.UserService;
import site.kuril.homepageblogbackend.vo.UserVO;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员用户控制器
 * 
 * @Author Kuril
 */
@Api(tags = "管理员用户接口", description = "管理员用户管理相关接口")
@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户列表（管理员）
     */
    @ApiOperation(value = "获取用户列表", notes = "管理员获取所有用户列表")
    @GetMapping
    public Result<Map<String, Object>> getUserList(
            @ApiParam(value = "当前页码", defaultValue = "1") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页条数", defaultValue = "10") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(value = "用户状态", defaultValue = "all") @RequestParam(defaultValue = "all") String status,
            @ApiParam(value = "搜索关键词") @RequestParam(required = false) String search) {

        IPage<UserVO> pageResult = userService.getUserList(page, pageSize, status, search);
        
        Map<String, Object> result = new HashMap<>();
        result.put("total", pageResult.getTotal());
        result.put("users", pageResult.getRecords());
        
        return Result.success(result);
    }

    /**
     * 更新用户状态
     */
    @ApiOperation(value = "更新用户状态", notes = "管理员更新用户状态（激活/禁用）")
    @PatchMapping("/{id}/status")
    public Result<Boolean> updateUserStatus(
            @ApiParam(value = "用户ID", required = true) @PathVariable Integer id,
            @ApiParam(value = "用户状态", required = true, example = "active/inactive/banned") @RequestParam String status) {
        
        boolean success = userService.updateUserStatus(id, status);
        return Result.success(success);
    }
} 