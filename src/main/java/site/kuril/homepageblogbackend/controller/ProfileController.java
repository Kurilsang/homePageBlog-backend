package site.kuril.homepageblogbackend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.kuril.homepageblogbackend.common.Result;
import site.kuril.homepageblogbackend.dto.ProfileDTO;
import site.kuril.homepageblogbackend.service.ProfileService;
import site.kuril.homepageblogbackend.vo.ProfileVO;

/**
 * 个人资料控制器
 * 
 * @Author Kuril
 */
@Api(tags = "个人资料接口", description = "个人资料管理")
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    /**
     * 获取个人资料
     */
    @ApiOperation(value = "获取个人资料", notes = "获取博客主个人资料")
    @GetMapping
    public Result<ProfileVO> getProfile() {
        ProfileVO profile = profileService.getProfile();
        return Result.success(profile);
    }

    /**
     * 更新个人资料
     */
    @ApiOperation(value = "更新个人资料", notes = "更新博客主个人资料")
    @PutMapping
    public Result<Boolean> updateProfile(
            @ApiParam(value = "个人资料信息", required = true) @RequestBody ProfileDTO profileDTO) {
        boolean success = profileService.updateProfile(profileDTO);
        return Result.success(success);
    }
} 