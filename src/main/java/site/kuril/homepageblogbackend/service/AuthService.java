package site.kuril.homepageblogbackend.service;

import site.kuril.homepageblogbackend.dto.LoginDTO;
import site.kuril.homepageblogbackend.vo.AdminVO;
import site.kuril.homepageblogbackend.vo.UserVO;

import java.util.Map;

/**
 * 认证服务接口
 */
public interface AuthService {
    /**
     * 管理员登录
     *
     * @param loginDTO 登录信息
     * @return 包含token和用户信息的Map
     */
    Map<String, Object> adminLogin(LoginDTO loginDTO);

    /**
     * 管理员登出
     *
     * @return 是否成功
     */
    boolean adminLogout();

    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return 包含token和用户信息的Map
     */
    Map<String, Object> userLogin(LoginDTO loginDTO);

    /**
     * 用户登出
     *
     * @return 是否成功
     */
    boolean userLogout();

    /**
     * 获取当前登录的管理员信息
     *
     * @return 管理员信息
     */
    AdminVO getCurrentAdmin();

    /**
     * 获取当前登录的用户信息
     *
     * @return 用户信息
     */
    UserVO getCurrentUser();
} 