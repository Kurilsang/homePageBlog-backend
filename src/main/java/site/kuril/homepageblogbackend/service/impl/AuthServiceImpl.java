package site.kuril.homepageblogbackend.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.kuril.homepageblogbackend.common.component.JwtTokenProvider;
import site.kuril.homepageblogbackend.common.exception.UnauthorizedException;
import site.kuril.homepageblogbackend.common.util.UserContext;
import site.kuril.homepageblogbackend.dto.LoginDTO;
import site.kuril.homepageblogbackend.mapper.AdminMapper;
import site.kuril.homepageblogbackend.mapper.UserMapper;
import site.kuril.homepageblogbackend.model.Admin;
import site.kuril.homepageblogbackend.model.User;
import site.kuril.homepageblogbackend.service.AuthService;
import site.kuril.homepageblogbackend.vo.AdminVO;
import site.kuril.homepageblogbackend.vo.UserVO;

import java.time.LocalDateTime;
import java.util.Map;



/**
 * 认证服务实现类
 * 
 * @Author Kuril
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public Map<String, Object> adminLogin(LoginDTO loginDTO) {
        // 验证用户名和密码
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername, loginDTO.getUsername());
        Admin admin = adminMapper.selectOne(queryWrapper);

        if (admin == null || !verifyPassword(loginDTO.getPassword(), admin.getPassword())) {
            throw new UnauthorizedException("用户名或密码错误");
        }

        // 更新最后登录时间
        admin.setLastLogin(LocalDateTime.now());
        adminMapper.updateById(admin);

        // 生成JWT令牌和管理员信息
        AdminVO adminVO = new AdminVO();
        BeanUtils.copyProperties(admin, adminVO);
        return jwtTokenProvider.generateAdminToken(admin, adminVO);
    }

    @Override
    public boolean adminLogout() {
        // JWT无状态，客户端清除令牌即可
        return true;
    }

    @Override
    public Map<String, Object> userLogin(LoginDTO loginDTO) {
        // 验证用户名和密码
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = userMapper.selectOne(queryWrapper);

        if (user == null || !verifyPassword(loginDTO.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("用户名或密码错误");
        }

        // 检查用户状态
        if (!"active".equals(user.getStatus())) {
            throw new UnauthorizedException("账号已被禁用或未激活");
        }

        // 更新最后登录时间
        user.setLastLogin(LocalDateTime.now());
        userMapper.updateById(user);

        // 生成JWT令牌和用户信息
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return jwtTokenProvider.generateUserToken(user, userVO);
    }

    @Override
    public boolean userLogout() {
        // JWT无状态，客户端清除令牌即可
        return true;
    }

    @Override
    public AdminVO getCurrentAdmin() {
        // 从上下文获取当前管理员信息
        Integer userId = UserContext.getUserId();
        String role = UserContext.getRole();
        
        if (userId == null || !"admin".equals(role)) {
            throw new UnauthorizedException("未登录管理员账号");
        }
        
        Admin admin = adminMapper.selectById(userId);
        if (admin == null) {
            throw new UnauthorizedException("管理员账号不存在");
        }
        
        AdminVO adminVO = new AdminVO();
        BeanUtils.copyProperties(admin, adminVO);
        return adminVO;
    }

    @Override
    public UserVO getCurrentUser() {
        // 从上下文获取当前用户信息
        Integer userId = UserContext.getUserId();
        String role = UserContext.getRole();
        
        if (userId == null || !"user".equals(role)) {
            throw new UnauthorizedException("未登录用户账号");
        }
        
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new UnauthorizedException("用户账号不存在");
        }
        
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 验证密码
     *
     * @param inputPassword 输入密码
     * @param storedPassword 存储的密码哈希
     * @return 是否匹配
     */
    private boolean verifyPassword(String inputPassword, String storedPassword) {
        // 实际项目中应使用更安全的密码哈希算法
        log.info(SecureUtil.md5(inputPassword));
        return StrUtil.isNotEmpty(inputPassword) && 
               SecureUtil.md5(inputPassword).equals(storedPassword);
    }
} 