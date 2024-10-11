package com.jeonsaeyukjun.jeonsaeyukjunbe.agent.service;

import com.jeonsaeyukjun.jeonsaeyukjunbe.agent.Dto.kakaoMessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@PropertySource("classpath:application.properties")
@Service
public class kakaoMessageService {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    @Value("${kakao.api.url}")
    private String kakaoApiUrl;


    public String getKakaoToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoApiKey);
        params.add("redirect_uri", "http://localhost:8080/kakao/callback");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
            Map<String, Object> responseBody = response.getBody();
            return (String) responseBody.get("access_token");
        } catch (Exception e) {
            throw new RuntimeException("토큰 요청 실패", e);
        }
    }

    public ResponseEntity<String> sendKakaoMessage(kakaoMessageRequest request, String accessToken)  {
        accessToken = accessToken.replace("Bearer ", "");

        String kakaoApiUrl = "https://kapi.kakao.com/v2/api/talk/memo/send";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(accessToken);

        Map<String, String> templateArgs = new HashMap<>();
        templateArgs.put("userName", request.getUserName());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("template_id", "113000"); // 템플릿 ID
        params.add("template_args", "{\"userName\": \"" + request.getUserName() + "\"}");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(kakaoApiUrl, requestEntity, String.class);
            return ResponseEntity.ok("카카오톡 메시지가 성공적으로 전송되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("메시지 전송 실패");
        }
    }


}

