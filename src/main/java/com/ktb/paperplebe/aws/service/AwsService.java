package com.ktb.paperplebe.aws.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class AwsService {
    
    private final S3Presigner presigner;

    public String getPresignUrl(String filename) {
        if (filename == null || filename.equals("")) {
            return null;
        }

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket("ktb-test")
                .key(filename)
                .build();


        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .putObjectRequest(putObjectRequest)
                .build();


        PresignedPutObjectRequest presignedPutObjectRequest = presigner.presignPutObject(putObjectPresignRequest);

        String url = presignedPutObjectRequest.url().toString();

        presigner.close(); // presigner를 닫고 획득한 모든 리소스를 해제
        return url;
    }

}