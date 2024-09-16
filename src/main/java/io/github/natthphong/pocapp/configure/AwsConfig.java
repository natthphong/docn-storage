package io.github.natthphong.pocapp.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class AwsConfig {

    @Value("${do.spaces.key}")
    private String doSpaceKey;

    @Value("${do.spaces.secret}")
    private String doSpaceSecret;

    @Value("${do.spaces.endpoint}")
    private String doSpaceEndpoint;

    @Value("${do.spaces.region}")
    private String doSpaceRegion;

    private AwsBasicCredentials getAwsCredentials() {
        return AwsBasicCredentials.create(doSpaceKey, doSpaceSecret);
    }

    @Bean
    public S3AsyncClient createS3AsyncClient() {
        return S3AsyncClient.builder()
                .endpointOverride(URI.create(doSpaceEndpoint))
                .region(Region.of(doSpaceRegion))
                .credentialsProvider(StaticCredentialsProvider.create(getAwsCredentials()))
                .serviceConfiguration(S3Configuration.builder().build())
                .build();
    }


    @Bean
    public S3Presigner presigner() {
        return S3Presigner.builder()
                .endpointOverride(URI.create(doSpaceEndpoint))
                .region(Region.of(doSpaceRegion))
                .credentialsProvider(StaticCredentialsProvider.create(getAwsCredentials()))
                .serviceConfiguration(S3Configuration.builder().build())
                .build();
    }

    @Bean
    public S3Client createS3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create(doSpaceEndpoint))
                .region(Region.of(doSpaceRegion))
                .credentialsProvider(StaticCredentialsProvider.create(getAwsCredentials()))
                .serviceConfiguration(S3Configuration.builder().build())
                .build();
    }
}
