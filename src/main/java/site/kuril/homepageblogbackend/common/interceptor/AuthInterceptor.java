package site.kuril.homepageblogbackend.common.interceptor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import site.kuril.homepageblogbackend.common.config.WhiteListConfig;
import site.kuril.homepageblogbackend.common.constant.ErrorCode;
import site.kuril.homepageblogbackend.common.exception.ServiceException;
import site.kuril.homepageblogbackend.common.util.JwtUtil;
import site.kuril.homepageblogbackend.common.util.UserContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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
        /** 超级管理员路径 */
        SUPER_ADMIN,
        /** 普通路径 */
        NORMAL
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
     return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 清理用户上下文
        UserContext.clear();
    }
    
    /**
     * 一次性判断路径类型，避免多次匹配
     * @param requestPath 请求路径
     * @return 路径类型
     */
    private PathType getPathType(String requestPath) {
        // 优先检查白名单路径
        if (whiteListConfig.getUrls().stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, requestPath))) {
            return PathType.WHITE;
        }
        
        // 检查超级管理员路径
        if (whiteListConfig.getSuperAdminUrls().stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, requestPath))) {
            return PathType.SUPER_ADMIN;
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