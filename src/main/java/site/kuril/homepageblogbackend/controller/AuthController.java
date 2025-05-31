package site.kuril.homepageblogbackend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.kuril.homepageblogbackend.common.Result;
import site.kuril.homepageblogbackend.dto.LoginDTO;
import site.kuril.homepageblogbackend.service.AuthService;

import javax.validation.Valid;
import java.util.Map;

/**
 * 认证控制器
 * 
 * @Author Kuril
 */
@Api(tags = "认证接口", description = "用户登录登出相关接口")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 管理员登录
     */
    @ApiOperation(value = "管理员登录", notes = "管理员用户名密码登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginDTO loginDTO) {
        Map<String, Object> result = authService.adminLogin(loginDTO);
        return Result.success(result);
    }

    /**
     * 管理员登出
     */
    @ApiOperation(value = "管理员登出", notes = "管理员退出登录")
    @PostMapping("/logout")
    public Result<Boolean> logout() {
        boolean result = authService.adminLogout();
        return Result.success(result);
    }
} 