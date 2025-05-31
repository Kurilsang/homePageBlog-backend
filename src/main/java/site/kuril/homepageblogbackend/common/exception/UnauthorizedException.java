package site.kuril.homepageblogbackend.common.exception;

/**
 * 未授权异常
 * 
 * @Author Kuril
 */
public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException(String message) {
        super(message);
    }
} 