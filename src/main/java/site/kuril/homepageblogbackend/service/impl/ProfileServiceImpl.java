package site.kuril.homepageblogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import site.kuril.homepageblogbackend.dto.ProfileDTO;
import site.kuril.homepageblogbackend.mapper.ProfileMapper;
import site.kuril.homepageblogbackend.model.Profile;
import site.kuril.homepageblogbackend.service.ProfileService;
import site.kuril.homepageblogbackend.vo.ProfileVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 个人资料服务实现类
 */
@Service
public class ProfileServiceImpl extends ServiceImpl<ProfileMapper, Profile> implements ProfileService {

    @Override
    public ProfileVO getProfile() {
        // 获取个人资料（应该只有一条记录）
        LambdaQueryWrapper<Profile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Profile::getId);
        queryWrapper.last("LIMIT 1");
        Profile profile = getOne(queryWrapper);

        // 如果没有记录，返回空对象
        if (profile == null) {
            return new ProfileVO();
        }

        // 转换为VO
        ProfileVO profileVO = new ProfileVO();
        BeanUtils.copyProperties(profile, profileVO);
        return profileVO;
    }

    @Override
    public boolean updateProfile(ProfileDTO profileDTO) {
        // 查询是否已有记录
        LambdaQueryWrapper<Profile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Profile::getId);
        queryWrapper.last("LIMIT 1");
        Profile profile = getOne(queryWrapper);

        // 如果没有记录，创建一条新记录
        if (profile == null) {
            profile = new Profile();
            profile.setCreatedAt(LocalDateTime.now());
        }

        // 更新资料
        BeanUtils.copyProperties(profileDTO, profile);
        profile.setUpdatedAt(LocalDateTime.now());

        // 保存或更新
        return saveOrUpdate(profile);
    }
} 