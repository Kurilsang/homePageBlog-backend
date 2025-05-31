package site.kuril.homepageblogbackend.common.util;

/**
 * 用户上下文工具类
 * 
 * @Author Kuril
 */
public class UserContext {
    
    private static final ThreadLocal<Integer> userIdHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> roleHolder = new ThreadLocal<>();
    
    /**
     * 设置用户ID
     * 
     * @param userId 用户ID
     */
    public static void setUserId(Integer userId) {
        userIdHolder.set(userId);
    }
    
    /**
     * 获取用户ID
     * 
     * @return 用户ID
     */
    public static Integer getUserId() {
        return userIdHolder.get();
    }
    
    /**
     * 设置用户角色
     * 
     * @param role 用户角色
     */
    public static void setRole(String role) {
        roleHolder.set(role);
    }
    
    /**
     * 获取用户角色
     * 
     * @return 用户角色
     */
    public static String getRole() {
        return roleHolder.get();
    }
    
    /**
     * 判断当前用户是否是管理员
     * 
     * @return 是否为管理员
     */
    public static boolean isAdmin() {
        String role = getRole();
        return role != null && "admin".equals(role);
    }
    
    /**
     * 清理上下文
     */
    public static void clear() {
        userIdHolder.remove();
        roleHolder.remove();
    }
} 