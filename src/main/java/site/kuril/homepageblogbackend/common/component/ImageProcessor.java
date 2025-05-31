package site.kuril.homepageblogbackend.common.component;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * 图片处理组件
 * 
 * @Author Kuril
 */
@Slf4j
@Component
public class ImageProcessor {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final Set<String> IMAGE_EXTENSIONS = new HashSet<>(
            Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "webp"));
    
    /**
     * 检查文件是否为图片
     *
     * @param file 上传的文件
     * @return 是否为图片
     */
    public boolean isImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        
        // 检查文件扩展名
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (extension == null) {
            return false;
        }
        
        return IMAGE_EXTENSIONS.contains(extension.toLowerCase());
    }
    
    /**
     * 生成基于时间戳和UUID的唯一文件名
     *
     * @param extension 文件扩展名
     * @return 唯一文件名
     */
    private String generateUniqueFileName(String extension) {
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        
        return timestamp + "_" + uuid + "." + extension;
    }
    
    /**
     * 将图片转换为WebP格式
     *
     * @param file 原始图片文件
     * @return WebP格式的文件输入流和文件名
     * @throws IOException 如果转换过程中发生错误
     */
    public WebpResult convertToWebp(MultipartFile file) throws IOException {
        try {
            // 读取原始图片
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            if (originalImage == null) {
                log.error("无法读取图片: {}", file.getOriginalFilename());
                throw new IOException("无法读取图片");
            }
            
            // 压缩图片（可选）
            BufferedImage compressedImage = Thumbnails.of(originalImage)
                    .scale(1.0) // 保持原始大小
                    .outputQuality(0.85) // 设置质量
                    .asBufferedImage();
            
            // 转换为WebP
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(compressedImage, "webp", outputStream);
            
            // 创建输入流
            InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            
            // 生成基于时间戳和UUID的唯一文件名
            String newFilename = generateUniqueFileName("webp");
            
            return new WebpResult(inputStream, newFilename, outputStream.size());
        } catch (Exception e) {
            log.error("转换WebP失败: {}", e.getMessage(), e);
            throw new IOException("转换WebP失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * WebP转换结果类
     */
    public static class WebpResult {
        private final InputStream inputStream;
        private final String filename;
        private final long size;
        
        public WebpResult(InputStream inputStream, String filename, long size) {
            this.inputStream = inputStream;
            this.filename = filename;
            this.size = size;
        }
        
        public InputStream getInputStream() {
            return inputStream;
        }
        
        public String getFilename() {
            return filename;
        }
        
        public long getSize() {
            return size;
        }
    }
} 