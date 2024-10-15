package com.jeonsaeyukjun.jeonsaeyukjunbe.contract.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto.ContractTableDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.mapper.FileMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public String generateS3Url(String contractName) {
        return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + contractName;
    }

    private final FileMapper fileMapper;
    private final AmazonS3 amazonS3;

    @Autowired
    public S3Service(AmazonS3 amazonS3, FileMapper fileMapper) {
        this.amazonS3 = amazonS3;
        this.fileMapper = fileMapper;
    }


    public String uploadFileAndSaveToDb(ByteArrayInputStream inputStream, String fileName, long contentLength, Integer reportId, int userId){

        // 랜덤 UID 생성 후 파일 이름 지정
        String contractName = UUID.randomUUID() + "_" + fileName;
        log.info("PDF 이름 key: " + contractName);

        // 파일의 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/pdf");
        metadata.setContentLength(contentLength);

        try {

            if (bucket == null || bucket.isEmpty()) {
                log.error("버킷 이름이 null 또는 빈 문자열입니다.");
                throw new IllegalArgumentException("Bucket name must be specified");
            }

            // 파일 S3에 저장
            PutObjectRequest request = new PutObjectRequest(bucket, contractName, inputStream, metadata);
            amazonS3.putObject(request);

            // 파일명 MySQL에 저장
            ContractTableDto fileDto = new ContractTableDto();
            fileDto.setContractName(contractName);

            String updateUrl = generateS3Url(contractName);
            fileDto.setContractUrl(updateUrl);
            fileDto.setUserId(userId);

            // reportId가 없으면 NULL로 처리
            if (reportId == null) {
                fileDto.setReportId(reportId); // reportId가 있을 경우 설정
            } else {
                fileDto.setReportId(null); // reportId가 없으면 NULL 설정
            }

            fileMapper.insertContract(fileDto);
            return contractName;

        } catch (SdkClientException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public String generatePresignedUrl(String fileName) {
        // URL 만료 시간 설정
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60; // 1시간
        expiration.setTime(expTimeMillis);

        // 서명된 URL 요청 생성
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, fileName)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

}




