package com.jeonsaeyukjun.jeonsaeyukjunbe.S3.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.jeonsaeyukjun.jeonsaeyukjunbe.S3.Dto.FileDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.S3.mapper.FileMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

@Slf4j
@Service
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    private final FileMapper fileMapper;
    private final AmazonS3 amazonS3;

    @Autowired
    public S3Service(AmazonS3 amazonS3, FileMapper fileMapper) {
        this.amazonS3 = amazonS3;
        this.fileMapper = fileMapper;
    }

    public String uploadFileAndSaveToDb(MultipartFile file){
        // 랜덤 UID 생성 후 파일 이름 지정
        String contractName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        log.info("PDF 이름 key: " + contractName);

        // 파일의 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try {

            if (bucket == null || bucket.isEmpty()) {
                log.error("버킷 이름이 null 또는 빈 문자열입니다.");
                throw new IllegalArgumentException("Bucket name must be specified");
            }

            // 파일 S3에 저장
            PutObjectRequest request = new PutObjectRequest(bucket, contractName, file.getInputStream(), metadata);
            amazonS3.putObject(request);

            // 임의로 Id 값 고정
            int userId = 1;
            int reportId = 1;

            // 파일명 MySQL에 저장
            FileDto fileDto = new FileDto();
            fileDto.setContractName(contractName);
            String updateUrl = fileDto.updateContractUrl(fileDto.getContractUrl(contractName));
            fileDto.setContractUrl(updateUrl);
            fileDto.setUserId(userId);
            fileDto.setReportId(reportId);

            fileMapper.insertContract(fileDto);


            return contractName;

        } catch (SdkClientException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public UrlResource downloadFile(String contractName) throws MalformedURLException, FileNotFoundException {
        // DB에서 파일명 검색 후 S3에서 다운로드
        FileDto fileDto = fileMapper.findByContractName(contractName);
        if (fileDto == null) {
            throw new FileNotFoundException("파일을 찾을 수 없습니다.");
        }

        // AWS S3 클라이언트를 통해 S3 URL 생성 (AWS SDK 이용)
        String fileName = fileDto.getContractName();
        String fileUrl = "https://s3.ap-northeast-2.amazonaws.com/" + bucket + "/" + fileName;

        log.info("파일 URL: {}", fileUrl);

        return new UrlResource(fileUrl);
    }

}




