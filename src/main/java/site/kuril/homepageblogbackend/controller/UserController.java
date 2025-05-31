package site.kuril.homepageblogbackend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.kuril.homepageblogbackend.common.Result;
import site.kuril.homepageblogbackend.dto.LoginDTO;
import site.kuril.homepageblogbackend.dto.PasswordUpdateDTO;
import site.kuril.homepageblogbackend.dto.UserProfileUpdateDTO;
import site.kuril.homepageblogbackend.dto.UserRegisterDTO;
import site.kuril.homepageblogbackend.service.AuthService;
import site.kuril.homepageblogbackend.service.CommentService;
import site.kuril.homepageblogbackend.service.UserService;
import site.kuril.homepageblogbackend.vo.CommentVO;
import site.kuril.homepageblogbackend.vo.UserVO;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 * 
 * @Author Kuril
 */
@Api(tags = "用户接口", description = "用户注册、登录、信息管理")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private CommentService commentService;

    /**
     * 用户注册
     */
    @ApiOperation(value = "用户注册", notes = "普通用户注册")
    @PostMapping("/register")
    public Result<Map<String, Object>> register(
            @ApiParam(value = "用户注册信息", required = true) @Valid @RequestBody UserRegisterDTO registerDTO) {
        boolean success = userService.register(registerDTO);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", "注册成功");
        
        return Result.success(result);
    }

    /**
     * 用户登录
     */
    @ApiOperation(value = "用户登录", notes = "普通用户登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(
            @ApiParam(value = "登录信息", required = true) @Valid @RequestBody LoginDTO loginDTO) {
        Map<String, Object> result = authService.userLogin(loginDTO);
        return Result.success(result);
    }

    /**
     * 用户登出
     */
    @ApiOperation(value = "用户登出", notes = "普通用户退出登录")
    @PostMapping("/logout")
    public Result<Boolean> logout() {
        boolean success = authService.userLogout();
        return Result.success(success);
    }

    /**
     * 获取用户信息
     */
    @ApiOperation(value = "获取用户信息", notes = "获取当前登录用户的个人信息")
    @GetMapping("/profile")
    public Result<UserVO> getUserInfo() {
        UserVO currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return Result.error("用户未登录");
        }
        
        UserVO userInfo = userService.getUserInfo(currentUser.getId());
        return Result.success(userInfo);
    }

    /**
     * 更新用户信息
     */
    @ApiOperation(value = "更新用户信息", notes = "更新当前登录用户的个人信息")
    @PutMapping("/profile")
    public Result<UserVO> updateUserInfo(
            @ApiParam(value = "用户信息", required = true) @RequestBody UserProfileUpdateDTO updateDTO) {
        UserVO currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return Result.error("用户未登录");
        }
        
        UserVO updatedUser = userService.updateUserInfo(currentUser.getId(), updateDTO);
        return Result.success(updatedUser);
    }

    /**
     * 修改密码
     */
    @ApiOperation(value = "修改密码", notes = "修改当前登录用户的密码")
    @PutMapping("/password")
    public Result<Boolean> updatePassword(
            @ApiParam(value = "密码更新信息", required = true) @Valid @RequestBody PasswordUpdateDTO passwordUpdateDTO) {
        UserVO currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return Result.error("用户未登录");
        }
        
        boolean success = userService.updatePassword(currentUser.getId(), passwordUpdateDTO);
        return Result.success(success);
    }

    /**
     * 获取用户评论列表
     */
    @ApiOperation(value = "获取用户评论列表", notes = "获取当前登录用户的所有评论")
    @GetMapping("/comments")
    public Result<Map<String, Object>> getUserComments(
            @ApiParam(value = "当前页码", defaultValue = "1") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页条数", defaultValue = "10") @RequestParam(defaultValue = "10") Integer pageSize) {
        
        UserVO currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return Result.error("用户未登录");
        }
        
        IPage<CommentVO> pageResult = commentService.getUserComments(page, pageSize, currentUser.getId());
        
        Map<String, Object> result = new HashMap<>();
        result.put("total", pageResult.getTotal());
        result.put("comments", pageResult.getRecords());
        
        return Result.success(result);
    }
} 