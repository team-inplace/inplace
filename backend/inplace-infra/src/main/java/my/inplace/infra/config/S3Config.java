package my.inplace.infra.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import my.inplace.infra.properties.AwsProperties;

@Configuration
@RequiredArgsConstructor
public class S3Config {
    private final AwsProperties awsProperties;

    @Bean
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsProperties.accessKey(),
                awsProperties.secretKey());

        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(awsProperties.region())
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}
