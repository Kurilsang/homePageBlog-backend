package site.kuril.homepageblogbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import site.kuril.homepageblogbackend.model.Comment;
import site.kuril.homepageblogbackend.vo.CommentVO;

import java.util.List;

/**
 * 评论Mapper接口
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    /**
     * 查询文章评论列表
     *
     * @param articleId 文章ID
     * @param status 评论状态（非管理员只能查看已审核评论）
     * @return 评论VO列表
     */
    List<CommentVO> selectArticleComments(@Param("articleId") Integer articleId, @Param("status") String status);

    /**
     * 分页查询所有评论（管理员使用）
     *
     * @param page 分页对象
     * @param status 评论状态筛选
     * @param search 搜索关键词
     * @param userType 用户类型筛选
     * @return 评论VO分页对象
     */
    IPage<CommentVO> selectCommentList(Page<Comment> page, @Param("status") String status, 
                                      @Param("search") String search, @Param("userType") String userType);

    /**
     * 分页查询用户评论列表
     *
     * @param page 分页对象
     * @param userId 用户ID
     * @return 评论VO分页对象
     */
    IPage<CommentVO> selectUserComments(Page<Comment> page, @Param("userId") Integer userId);
} 