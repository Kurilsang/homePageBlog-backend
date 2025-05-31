package site.kuril.homepageblogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.kuril.homepageblogbackend.dto.ProfileDTO;
import site.kuril.homepageblogbackend.model.Profile;
import site.kuril.homepageblogbackend.vo.ProfileVO;

/**
 * 个人资料服务接口
 */
public interface ProfileService extends IService<Profile> {
    /**
     * 获取个人资料
     *
     * @return 个人资料视图对象
     */
    ProfileVO getProfile();

    /**
     * 更新个人资料
     *
     * @param profileDTO 个人资料数据
     * @return 是否成功
     */
    boolean updateProfile(ProfileDTO profileDTO);
} 