package site.kuril.homepageblogbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import site.kuril.homepageblogbackend.model.User;
import site.kuril.homepageblogbackend.vo.UserVO;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 分页查询用户列表（包含评论数量）
     *
     * @param page 分页对象
     * @param status 状态筛选
     * @param search 搜索关键词
     * @return 用户VO分页列表
     */
    IPage<UserVO> selectUserList(Page<User> page, @Param("status") String status, @Param("search") String search);
}