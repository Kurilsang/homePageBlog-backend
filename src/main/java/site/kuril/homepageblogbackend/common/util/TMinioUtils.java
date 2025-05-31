package site.kuril.homepageblogbackend.common.util;

import io.minio.*;
import io.minio.errors.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Data
@Component
@Slf4j
public class TMinioUtils {
    @Autowired
    private Environment environment;
    
    private static final String CERT_PATH = "cert/minio-cert.crt";

    /**
     * 实现上传单个文件到Minio
     */
    public String upload(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();

        String ip_addr = environment.getProperty("my.minio.ip_addr");
        String bucketName = environment.getProperty("my.minio.bucketName");
        String filePath = environment.getProperty("my.minio.filePath");

        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));

        // 创建MinIO客户端，使用证书
        MinioClient minioClient = createMinioClientWithCert(
                ip_addr, 
                environment.getProperty("my.minio.access-key"), 
                environment.getProperty("my.minio.secret-key")
        );

        // 看一下bucket是否存在
        boolean found =
                minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            // 如果没有就创建一个bucket
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(filePath+fileName)
                .stream(inputStream,file.getSize(),-1)
                .contentType(file.getContentType())
                .build());
                
        String url = ip_addr+"/"+bucketName+filePath+fileName;

        return url;
    }
    
    /**
     * 实现上传多个文件到Minio
     */
    public ArrayList<String> multiUpload(MultipartFile[] files) throws Exception {
        ArrayList<String> urls = new ArrayList<>();
        
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    String url = upload(file);
                    urls.add(url);
                }
            }
        }
        
        return urls;
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
            log.warn("证书文件不存在: {}，将创建标准客户端", CERT_PATH);
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
