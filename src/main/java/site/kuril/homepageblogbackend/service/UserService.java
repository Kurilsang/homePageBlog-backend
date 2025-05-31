package site.kuril.homepageblogbackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import site.kuril.homepageblogbackend.dto.PasswordUpdateDTO;
import site.kuril.homepageblogbackend.dto.UserProfileUpdateDTO;
import site.kuril.homepageblogbackend.dto.UserRegisterDTO;
import site.kuril.homepageblogbackend.model.User;
import site.kuril.homepageblogbackend.vo.UserVO;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param registerDTO 注册信息
     * @return 是否成功
     */
    boolean register(UserRegisterDTO registerDTO);

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserVO getUserInfo(Integer userId);

    /**
     * 更新用户信息
     *
     * @param userId 用户ID
     * @param updateDTO 更新信息
     * @return 更新后的用户信息
     */
    UserVO updateUserInfo(Integer userId, UserProfileUpdateDTO updateDTO);

    /**
     * 修改密码
     *
     * @param userId 用户ID
     * @param passwordUpdateDTO 密码更新信息
     * @return 是否成功
     */
    boolean updatePassword(Integer userId, PasswordUpdateDTO passwordUpdateDTO);

    /**
     * 分页获取用户列表（管理员使用）
     *
     * @param page 当前页码
     * @param pageSize 每页条数
     * @param status 状态筛选
     * @param search 搜索关键词
     * @return 用户分页对象
     */
    IPage<UserVO> getUserList(Integer page, Integer pageSize, String status, String search);

    /**
     * 更新用户状态（管理员使用）
     *
     * @param userId 用户ID
     * @param status 新状态
     * @return 是否成功
     */
    boolean updateUserStatus(Integer userId, String status);
} 