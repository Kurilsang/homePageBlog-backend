package site.kuril.homepageblogbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import site.kuril.homepageblogbackend.model.Profile;

/**
 * 个人资料Mapper接口
 */
@Mapper
public interface ProfileMapper extends BaseMapper<Profile> {
} 