package site.kuril.homepageblogbackend.common.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.kuril.homepageblogbackend.common.util.JwtUtil;
import site.kuril.homepageblogbackend.model.Admin;
import site.kuril.homepageblogbackend.model.User;
import site.kuril.homepageblogbackend.vo.AdminVO;
import site.kuril.homepageblogbackend.vo.UserVO;

import java.util.HashMap;
import java.util.Map;

/**
 * JWT令牌提供者组件
 * 
 * @Author Kuril
 */
@Component
public class JwtTokenProvider {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 为管理员生成登录令牌和信息
     * 
     * @param admin 管理员
     * @param adminVO 管理员视图对象
     * @return 包含token和用户信息的Map
     */
    public Map<String, Object> generateAdminToken(Admin admin, AdminVO adminVO) {
        String token = jwtUtil.generateToken(admin.getId(), "admin");
        
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", adminVO);
        
        return result;
    }

    /**
     * 为用户生成登录令牌和信息
     * 
     * @param user 用户
     * @param userVO 用户视图对象
     * @return 包含token和用户信息的Map
     */
    public Map<String, Object> generateUserToken(User user, UserVO userVO) {
        String token = jwtUtil.generateToken(user.getId(), "user");
        
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", userVO);
        
        return result;
    }
} 