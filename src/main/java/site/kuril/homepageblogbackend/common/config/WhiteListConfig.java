package site.kuril.homepageblogbackend.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口访问白名单配置
 * 
 * @Author Kuril
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "security")
public class WhiteListConfig {
    /**
     * 无需认证的接口列表
     */
    private List<String> whiteList = new ArrayList<>();
    
    /**
     * 需要管理员权限的接口
     */
    private List<String> adminUrls = new ArrayList<>();
} 