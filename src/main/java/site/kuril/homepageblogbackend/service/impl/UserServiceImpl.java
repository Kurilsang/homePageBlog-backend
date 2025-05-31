package site.kuril.homepageblogbackend.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.kuril.homepageblogbackend.dto.PasswordUpdateDTO;
import site.kuril.homepageblogbackend.dto.UserProfileUpdateDTO;
import site.kuril.homepageblogbackend.dto.UserRegisterDTO;
import site.kuril.homepageblogbackend.mapper.UserMapper;
import site.kuril.homepageblogbackend.model.User;
import site.kuril.homepageblogbackend.service.UserService;
import site.kuril.homepageblogbackend.vo.UserVO;

import java.time.LocalDateTime;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean register(UserRegisterDTO registerDTO) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<User> usernameQuery = new LambdaQueryWrapper<>();
        usernameQuery.eq(User::getUsername, registerDTO.getUsername());
        if (count(usernameQuery) > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在（如果提供了邮箱）
        if (StrUtil.isNotBlank(registerDTO.getEmail())) {
            LambdaQueryWrapper<User> emailQuery = new LambdaQueryWrapper<>();
            emailQuery.eq(User::getEmail, registerDTO.getEmail());
            if (count(emailQuery) > 0) {
                throw new RuntimeException("邮箱已被注册");
            }
        }

        // 创建用户
        User user = new User();
        BeanUtils.copyProperties(registerDTO, user);

        // 密码加密
        user.setPassword(SecureUtil.md5(registerDTO.getPassword()));

        // 设置默认值
        user.setStatus("active");
        user.setAvatar("/images/avatars/default-avatar.jpg");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // 保存用户
        return save(user);
    }

    @Override
    public UserVO getUserInfo(Integer userId) {
        // 获取用户信息
        User user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 转换为VO
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public UserVO updateUserInfo(Integer userId, UserProfileUpdateDTO updateDTO) {
        // 获取用户信息
        User user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 检查邮箱是否已被其他用户使用
        if (StrUtil.isNotBlank(updateDTO.getEmail()) && !updateDTO.getEmail().equals(user.getEmail())) {
            LambdaQueryWrapper<User> emailQuery = new LambdaQueryWrapper<>();
            emailQuery.eq(User::getEmail, updateDTO.getEmail());
            emailQuery.ne(User::getId, userId);
            if (count(emailQuery) > 0) {
                throw new RuntimeException("邮箱已被其他用户注册");
            }
        }

        // 更新用户信息
        if (StrUtil.isNotBlank(updateDTO.getNickname())) {
            user.setNickname(updateDTO.getNickname());
        }
        if (StrUtil.isNotBlank(updateDTO.getEmail())) {
            user.setEmail(updateDTO.getEmail());
        }
        if (StrUtil.isNotBlank(updateDTO.getAvatar())) {
            user.setAvatar(updateDTO.getAvatar());
        }
        user.setUpdatedAt(LocalDateTime.now());
        updateById(user);

        // 返回更新后的用户信息
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public boolean updatePassword(Integer userId, PasswordUpdateDTO passwordUpdateDTO) {
        // 获取用户信息
        User user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证当前密码
        String currentPasswordHash = SecureUtil.md5(passwordUpdateDTO.getCurrentPassword());
        if (!currentPasswordHash.equals(user.getPassword())) {
            throw new RuntimeException("当前密码错误");
        }

        // 更新密码
        user.setPassword(SecureUtil.md5(passwordUpdateDTO.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        return updateById(user);
    }

    @Override
    public IPage<UserVO> getUserList(Integer page, Integer pageSize, String status, String search) {
        Page<User> pageParam = new Page<>(page, pageSize);
        return userMapper.selectUserList(pageParam, status, search);
    }

    @Override
    public boolean updateUserStatus(Integer userId, String status) {
        // 获取用户信息
        User user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 检查状态是否有效
        if (!status.equals("active") && !status.equals("inactive") && !status.equals("banned")) {
            throw new RuntimeException("无效的用户状态");
        }

        // 更新状态
        user.setStatus(status);
        user.setUpdatedAt(LocalDateTime.now());
        return updateById(user);
    }
} 