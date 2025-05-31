package site.kuril.homepageblogbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import site.kuril.homepageblogbackend.model.Setting;

/**
 * 系统设置Mapper接口
 */
@Mapper
public interface SettingMapper extends BaseMapper<Setting> {
    /**
     * 根据键名获取设置值
     *
     * @param key 设置键名
     * @return 设置值
     */
    String getSettingValue(@Param("key") String key);
} 