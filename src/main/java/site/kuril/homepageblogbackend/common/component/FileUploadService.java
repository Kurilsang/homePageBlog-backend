package site.kuril.homepageblogbackend.common.component;

import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import site.kuril.homepageblogbackend.common.util.TMinioUtils;

import javax.net.ssl.*;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 文件上传服务组件
 * 
 * @Author Kuril
 */
@Slf4j
@Component
public class FileUploadService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final String CERT_PATH = "cert/minio-cert.crt";

    @Autowired
    private Environment environment;
    
    @Autowired
    private TMinioUtils minioUtils;
    
    @Autowired
    private ImageProcessor imageProcessor;
    
    /**
     * 上传图片文件，如果是图片则转换为WebP格式
     * 
     * @param file 上传的文件
     * @param directory 目录路径，例如 "/avatar" 或 "/article"
     * @return 文件访问URL
     * @throws Exception 如果上传过程中发生错误
     */
    public String uploadImage(MultipartFile file, String directory) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }
        
        // 检查是否为图片
        if (imageProcessor.isImage(file)) {
            // 是图片，转换为WebP
            return uploadWebpImage(file, directory);
        } else {
            // 不是图片，直接上传
            return uploadFile(file, directory);
        }
    }
    
    /**
     * 生成唯一文件名
     * 
     * @param originalFilename 原始文件名
     * @return 基于时间戳和UUID的唯一文件名
     */
    private String generateUniqueFileName(String originalFilename) {
        String extension = FilenameUtils.getExtension(originalFilename);
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        
        return timestamp + "_" + uuid + "." + extension;
    }
    
    /**
     * 上传普通文件
     * 
     * @param file 上传的文件
     * @param directory 目录路径
     * @return 文件访问URL
     * @throws Exception 如果上传过程中发生错误
     */
    public String uploadFile(MultipartFile file, String directory) throws Exception {
        // 规范化目录路径
        String filePath = normalizePath(directory);
        
        // 获取MinIO配置
        String ipAddr = environment.getProperty("my.minio.ip_addr");
        String bucketName = environment.getProperty("my.minio.bucketName");
        String accessKey = environment.getProperty("my.minio.access-key");
        String secretKey = environment.getProperty("my.minio.secret-key");
        
        // 生成基于时间戳和UUID的唯一文件名
        String originalFilename = file.getOriginalFilename();
        String fileName = generateUniqueFileName(originalFilename);
        
        // 创建MinIO客户端，使用证书
        MinioClient minioClient = createMinioClientWithCert(ipAddr, accessKey, secretKey);
        
        // 检查并创建存储桶
        boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!bucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
        
        // 上传文件
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(filePath + fileName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build());
        
        // 返回文件URL
        return ipAddr + "/" + bucketName + filePath + fileName;
    }
    
    /**
     * 上传WebP格式的图片
     * 
     * @param file 原始图片文件
     * @param directory 目录路径
     * @return 文件访问URL
     * @throws Exception 如果上传过程中发生错误
     */
    private String uploadWebpImage(MultipartFile file, String directory) throws Exception {
        // 转换为WebP格式
        ImageProcessor.WebpResult webpResult = imageProcessor.convertToWebp(file);
        
        // 规范化目录路径
        String filePath = normalizePath(directory);
        
        // 获取MinIO配置
        String ipAddr = environment.getProperty("my.minio.ip_addr");
        String bucketName = environment.getProperty("my.minio.bucketName");
        String accessKey = environment.getProperty("my.minio.access-key");
        String secretKey = environment.getProperty("my.minio.secret-key");
        
        // 基于时间戳生成文件名
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String fileName = timestamp + "_" + uuid + ".webp";
        
        // 创建MinIO客户端，使用证书
        MinioClient minioClient = createMinioClientWithCert(ipAddr, accessKey, secretKey);
        
        // 检查并创建存储桶
        boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!bucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
        
        // 上传WebP文件
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(filePath + fileName)
                .stream(webpResult.getInputStream(), webpResult.getSize(), -1)
                .contentType("image/webp")
                .build());
        
        // 返回文件URL
        return ipAddr + "/" + bucketName + filePath + fileName;
    }
    
    /**
     * 规范化路径，确保以/开头和结尾
     * 
     * @param path 原始路径
     * @return 规范化后的路径
     */
    private String normalizePath(String path) {
        if (path == null || path.isEmpty()) {
            return "/";
        }
        
        StringBuilder sb = new StringBuilder(path);
        if (!path.startsWith("/")) {
            sb.insert(0, "/");
        }
        if (!path.endsWith("/")) {
            sb.append("/");
        }
        
        return sb.toString();
    }
    
    /**
     * 创建使用证书的MinIO客户端
     * 
     * @param endpoint MinIO服务器地址
     * @param accessKey 访问密钥
     * @param secretKey 秘密密钥
     * @return MinIO客户端实例
     * @throws Exception 如果创建客户端失败
     */
    private MinioClient createMinioClientWithCert(String endpoint, String accessKey, String secretKey) 
            throws Exception {
        // 加载证书文件
        ClassPathResource resource = new ClassPathResource(CERT_PATH);
        if (!resource.exists()) {
            log.warn("证书文件不存在: {}，将创建不验证证书的客户端", CERT_PATH);
            // 如果证书不存在，返回标准客户端
            return MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();
        }
        
        try (InputStream certInputStream = resource.getInputStream()) {
            // 创建证书工厂
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(certInputStream);
            
            // 创建信任库
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null); // 初始化空的KeyStore
            trustStore.setCertificateEntry("minio-cert", cert);
            
            // 创建信任管理器工厂
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);
            
            // 创建SSL上下文
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            
            // 创建OkHttpClient
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) tmf.getTrustManagers()[0])
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
            
            // 创建MinIO客户端
            return MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .httpClient(httpClient)
                    .build();
        } catch (Exception e) {
            log.error("创建安全MinIO客户端失败: {}", e.getMessage(), e);
            throw e;
        }
    }
} 