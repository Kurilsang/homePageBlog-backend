package site.kuril.homepageblogbackend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.kuril.homepageblogbackend.common.Result;
import site.kuril.homepageblogbackend.dto.ArticleDTO;
import site.kuril.homepageblogbackend.service.ArticleService;
import site.kuril.homepageblogbackend.vo.ArticleListVO;
import site.kuril.homepageblogbackend.vo.ArticleVO;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 文章控制器
 * 
 * @Author Kuril
 */
@Api(tags = "文章接口", description = "文章的增删改查操作")
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 获取文章列表
     */
    @ApiOperation(value = "获取文章列表", notes = "分页获取文章列表，支持搜索、标签筛选和置顶筛选")
    @GetMapping
    public Result<Map<String, Object>> getArticleList(
            @ApiParam(value = "当前页码", defaultValue = "1") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页条数", defaultValue = "10") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(value = "搜索关键词") @RequestParam(required = false) String search,
            @ApiParam(value = "标签筛选") @RequestParam(required = false) String tag,
            @ApiParam(value = "是否只获取置顶文章") @RequestParam(required = false) Boolean pinned) {

        IPage<ArticleListVO> pageResult = articleService.getArticleList(page, pageSize, search, tag, pinned);
        
        Map<String, Object> result = new HashMap<>();
        result.put("total", pageResult.getTotal());
        result.put("articles", pageResult.getRecords());
        
        return Result.success(result);
    }

    /**
     * 获取文章详情
     */
    @ApiOperation(value = "获取文章详情", notes = "根据文章ID获取文章详情")
    @GetMapping("/{id}")
    public Result<ArticleVO> getArticleDetail(
            @ApiParam(value = "文章ID", required = true) @PathVariable Integer id) {
        ArticleVO article = articleService.getArticleDetail(id);
        // 增加浏览次数
        articleService.increaseViewCount(id);
        return Result.success(article);
    }

    /**
     * 创建文章
     */
    @ApiOperation(value = "创建文章", notes = "创建新文章")
    @PostMapping
    public Result<Map<String, Object>> createArticle(
            @ApiParam(value = "文章信息", required = true) @Valid @RequestBody ArticleDTO articleDTO) {
        Integer id = articleService.createArticle(articleDTO);
        
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("success", true);
        
        return Result.success(result);
    }

    /**
     * 更新文章
     */
    @ApiOperation(value = "更新文章", notes = "根据文章ID更新文章信息")
    @PutMapping("/{id}")
    public Result<Boolean> updateArticle(
            @ApiParam(value = "文章ID", required = true) @PathVariable Integer id,
            @ApiParam(value = "文章信息", required = true) @Valid @RequestBody ArticleDTO articleDTO) {
        boolean success = articleService.updateArticle(id, articleDTO);
        return Result.success(success);
    }

    /**
     * 切换文章置顶状态
     */
    @ApiOperation(value = "切换文章置顶状态", notes = "设置或取消文章的置顶状态")
    @PatchMapping("/{id}/toggle-pin")
    public Result<Boolean> toggleArticlePin(
            @ApiParam(value = "文章ID", required = true) @PathVariable Integer id,
            @ApiParam(value = "是否置顶", required = true) @RequestParam Boolean isPinned) {
        boolean success = articleService.toggleArticlePin(id, isPinned);
        return Result.success(success);
    }

    /**
     * 删除文章
     */
    @ApiOperation(value = "删除文章", notes = "根据文章ID删除文章")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteArticle(
            @ApiParam(value = "文章ID", required = true) @PathVariable Integer id) {
        boolean success = articleService.deleteArticle(id);
        return Result.success(success);
    }
} 