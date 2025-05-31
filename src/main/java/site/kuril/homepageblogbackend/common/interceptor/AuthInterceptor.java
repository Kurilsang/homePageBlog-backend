package site.kuril.homepageblogbackend.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import site.kuril.homepageblogbackend.common.config.WhiteListConfig;
import site.kuril.homepageblogbackend.common.exception.UnauthorizedException;
import site.kuril.homepageblogbackend.common.util.JwtUtil;
import site.kuril.homepageblogbackend.common.util.UserContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证拦截器
 * 
 * @Author Kuril
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private WhiteListConfig whiteListConfig;

    private final PathMatcher pathMatcher = new AntPathMatcher();
    
    /**
     * 路径类型枚举
     */
    private enum PathType {
        /** 白名单路径 */
        WHITE,
        /** 管理员路径 */
        ADMIN,
        /** 普通路径 */
        NORMAL
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取请求路径并进行一次性匹配分类
        String requestPath = request.getRequestURI();
        PathType pathType = getPathType(requestPath);
        
        log.debug("请求路径：{}，路径类型：{}", requestPath, pathType);
        
        // 如果是白名单路径，直接放行
        if (pathType == PathType.WHITE) {
            return true;
        }

        // 获取token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 验证token
        if (token == null || token.isEmpty()) {
            log.warn("认证失败：未提供令牌，请求URL: {}", requestPath);
            throw new UnauthorizedException("请先登录");
        }

        // 验证token是否有效
        if (!jwtUtil.validateToken(token)) {
            log.warn("认证失败：无效的令牌");
            throw new UnauthorizedException("登录已过期，请重新登录");
        }

        // 获取用户ID并存入上下文
        Integer userId = jwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            log.warn("认证失败：令牌中无用户ID");
            throw new UnauthorizedException("无效的登录信息");
        }
        UserContext.setUserId(userId);
        
        // 获取token中的角色
        String role = jwtUtil.getRoleFromToken(token);
        if (role == null) {
            log.warn("认证失败：令牌中无用户角色");
            throw new UnauthorizedException("无效的登录信息");
        }
        
        // 将验证通过的角色存入上下文
        UserContext.setRole(role);
        
        // 如果是管理员接口，检查用户是否有管理员权限
        if (pathType == PathType.ADMIN && !"admin".equals(role)) {
            log.warn("权限不足：用户尝试访问管理员接口，用户ID={}, 角色={}", userId, role);
            throw new UnauthorizedException("无权访问管理员接口");
        }

     return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 清理用户上下文
        UserContext.clear();
    }
    
    /**
     * 一次性判断路径类型，避免多次匹配
     * 
     * @param requestPath 请求路径
     * @return 路径类型
     */
    private PathType getPathType(String requestPath) {
        // 优先检查白名单路径
        if (whiteListConfig.getWhiteList().stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, requestPath))) {
            return PathType.WHITE;
        }
        
        // 检查管理员路径
        if (whiteListConfig.getAdminUrls().stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, requestPath))) {
            return PathType.ADMIN;
        }
        
        // 默认为普通路径
        return PathType.NORMAL;
    }
} 