package io.github.natthphong.pocapp.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FileStorageComponent {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${do.spaces.bucket}")
    private String bucketName;

    @Value("${do.spaces.url.exp}")
    private Long timeExpUrl;

    public FileStorageComponent(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }


    public void uploadFile(MultipartFile file, String folder) throws IOException {
        String key = generateKey(folder, file.getOriginalFilename());
        uploadFile(file.getInputStream(), file.getSize(), file.getContentType(), key);
    }


    public void uploadFile(byte[] bytes, String contentType, String key) {
        s3Client.putObject(createPutObjectRequest(key, contentType), RequestBody.fromBytes(bytes));
    }


    public void uploadFile(byte[] bytes, String contentType, String folder, String fileName) {
        String key = generateKey(folder, fileName);
        uploadFile(bytes, contentType, key);
    }


    public void uploadFile(ByteArrayInputStream bytes, String contentType, String key) throws IOException {
        try (InputStream inputStream = new ByteArrayInputStream(bytes.readAllBytes())) {
            s3Client.putObject(createPutObjectRequest(key, contentType), RequestBody.fromInputStream(inputStream, bytes.available()));
        }
    }


    public void uploadFile(ByteArrayInputStream bytes, String contentType, String folder, String fileName) throws IOException {
        String key = generateKey(folder, fileName);
        uploadFile(bytes, contentType, key);
    }


    public void uploadFile(InputStream inputStream, long size, String contentType, String key) {
        s3Client.putObject(createPutObjectRequest(key, contentType), RequestBody.fromInputStream(inputStream, size));
    }


    public void uploadFile(InputStream inputStream, long size, String contentType, String folder, String fileName) {
        String key = generateKey(folder, fileName);
        uploadFile(inputStream, size, contentType, key);
    }


    public URL getFileUrl(String key) {
        GetObjectRequest getObjectRequest = createGetObjectRequest(key);
        GetObjectPresignRequest presignRequest = createPresignRequest(getObjectRequest);
        return s3Presigner.presignGetObject(presignRequest).url();
    }


    public URL getFileUrl(String folder, String fileName) {
        String key = generateKey(folder, fileName);
        return getFileUrl(key);
    }


    public List<String> listFiles(String folder) {
        ListObjectsV2Request request = createListObjectsRequest(folder);
        ListObjectsV2Response response = s3Client.listObjectsV2(request);
        return response.contents().stream().map(S3Object::key).collect(Collectors.toList());
    }


    public void deleteFile(String key) {
        DeleteObjectRequest deleteObjectRequest = createDeleteObjectRequest(key);
        s3Client.deleteObject(deleteObjectRequest);
    }


    public void deleteFile(String folder, String fileName) {
        String key = generateKey(folder, fileName);
        deleteFile(key);
    }


    private String generateKey(String folder, String fileName) {
        String extension = FilenameUtils.getExtension(fileName);
        String nameWithoutExtension = FilenameUtils.removeExtension(fileName);
        return folder + "/" + nameWithoutExtension + "." + extension;
    }

    public ResponseInputStream<GetObjectResponse> getObject(String key) {
        GetObjectRequest getObjectRequest = createGetObjectRequest(key);
        return s3Client.getObject(getObjectRequest);
    }

    public ResponseInputStream<GetObjectResponse> getObject(String folder, String fileName) {
        String key = generateKey(folder, fileName);
        return getObject(key);
    }

    public ListObjectsV2Response listFilesWithResponse(String folder) {
        ListObjectsV2Request request = createListObjectsRequest(folder);
        return s3Client.listObjectsV2(request);
    }


    private PutObjectRequest createPutObjectRequest(String key, String contentType) {
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();
    }


    private GetObjectRequest createGetObjectRequest(String key) {
        return GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
    }


    private GetObjectPresignRequest createPresignRequest(GetObjectRequest getObjectRequest) {
        return GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(timeExpUrl))
                .getObjectRequest(getObjectRequest)
                .build();
    }


    private ListObjectsV2Request createListObjectsRequest(String folder) {
        return ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(folder.endsWith("/") ? folder : folder + "/")
                .build();
    }


    private DeleteObjectRequest createDeleteObjectRequest(String key) {
        return DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
    }

}
