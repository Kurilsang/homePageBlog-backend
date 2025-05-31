package site.kuril.homepageblogbackend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.kuril.homepageblogbackend.common.Result;
import site.kuril.homepageblogbackend.common.component.FileUploadService;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传控制器
 * 
 * @Author Kuril
 */
@Api(tags = "文件上传接口", description = "文件上传相关接口")
@Slf4j
@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;
    
    /**
     * 上传图片，自动转换为WebP格式
     */
    @ApiOperation(value = "上传图片", notes = "上传图片文件，自动检测是否为图片，如果是则转换为WebP格式")
    @PostMapping("/image")
    public Result<Map<String, String>> uploadImage(
            @ApiParam(value = "图片文件", required = true) @RequestParam("file") MultipartFile file,
            @ApiParam(value = "存储目录，例如：avatar、article", required = true) @RequestParam("directory") String directory) {
        
        try {
            // 上传图片
            String fileUrl = fileUploadService.uploadImage(file, directory);
            
            // 返回结果
            Map<String, String> result = new HashMap<>();
            result.put("url", fileUrl);
            result.put("filename", file.getOriginalFilename());
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("上传图片失败: {}", e.getMessage(), e);
            return Result.error("上传图片失败: " + e.getMessage());
        }
    }
    
    /**
     * 上传文件（不进行WebP转换）
     */
    @ApiOperation(value = "上传文件", notes = "上传普通文件，不进行格式转换")
    @PostMapping("/file")
    public Result<Map<String, String>> uploadFile(
            @ApiParam(value = "文件", required = true) @RequestParam("file") MultipartFile file,
            @ApiParam(value = "存储目录，例如：documents、videos", required = true) @RequestParam("directory") String directory) {
        
        try {
            // 上传文件
            String fileUrl = fileUploadService.uploadFile(file, directory);
            
            // 返回结果
            Map<String, String> result = new HashMap<>();
            result.put("url", fileUrl);
            result.put("filename", file.getOriginalFilename());
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("上传文件失败: {}", e.getMessage(), e);
            return Result.error("上传文件失败: " + e.getMessage());
        }
    }
} 