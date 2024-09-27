package com.jeonsaeyukjun.jeonsaeyukjunbe.report.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class OpenAiService {

    private final RestTemplate restTemplate;

    @Value("${openai.api.key}")
    private String apiKey;

    public Map<String, Object> textToJsonWithOpenAI(StringBuilder text, String type) throws IOException {

        String request;

        if (type.equals("표제부")){
            request = "roadName(도로명주소를 찾아서 String)\n" +
                      "detailAddress (상세주소는 [집합 건물] 옆에 쓰인 내용 전체를 찾아서 String\n" +
                      "buildingType(건물용도는 건물 내역에 아파트, 주택등 부분을 찾아서 String)\n" +
                      "landType (토지 지목을 찾아서 String)\n" +
                      "landArea(토지면적을 찾아서 Double)\n" +
                      "buildingArea (건물면적을 찾아서 Double)\n" +
                      "위의 각 요소들을 찾고 json 형태로 반환해줘, 이때 키값은 왼쪽의 영어이름이고 값은 오른쪽 괄호안의 자료형에 맞게 값을 넣어줘";
        } else if (type.equals("갑구")) {
            request = "lessorName 소유자 이름 (마지막 소유권 이전의 소유자 이름을 찾아서 String)\n" +
                      "auctionRecord(등기 목적에서 경매개시결정을 찾아서 있는지 없는지를 찾아서 boolean)\n" +
                      "injuctionRecord(등기 목적에서 가처분이 있는지 없는지를 찾아서 boolean. 단 경매개시결정 이후에 있다면 없는거야)\n" +
                      "trustRegistrationRecord (신탁이 있는지 없는지를 찾아서 boolean)\n" +
                      "redemptionRecord(환매특약의 여부를 찾아서 boolean)\n" +
                      "registrationRecord (등기 목적에서 가등기가 있는지 없는지를 찾아서 boolean. 단 경매개시결정 이후에 있다면 없는거야)\n" +
                      "seizureCount(등기 목적에서 압류의 횟수를 찾아서 Integer. 말소가 있다면 해당 압류는 없는거야)\n" +
                      "provisionalSeizureCount(등기 목적에서 가압류의 횟수를 찾아서 Integer. 말소가 있다면 해당 가압류는 없는거야)" +
                      "위의 각 요소들을 찾고 json 형태로 반환해줘, 이때 키값은 왼쪽의 영어이름이고 값은 오른쪽 괄호안의 자료형에 맞게 값을 넣어줘";
        }else {
            request = "priorityDeposit (선순위 채권 총액 Long) \n" +
                      "mortgageCount(근저당권 설정 내역 부분을 찾아서 Integer, 단 해당하는 설정의 말소가 있다면 세지 말아줘)\n" +
                      "leaseholdRegistrationCount(전세권 설정 내역을 찾아서 Integer, 단 해당하는 설정의 말소가 있다면 세지 말아줘)" +
                      "위의 각 요소들을 찾고 json 형태로 반환해줘, 이때 키값은 왼쪽의 영어이름이고 값은 오른쪽 괄호안의 자료형에 맞게 값을 넣어줘";
        }


        List<Map<String, String>> messages = List.of(
                Map.of("role", "system", "content", "You are a helpful assistant."),
                Map.of("role", "user", "content", request + "\n" + text)
        );

        // 요청 본문 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o-mini");
        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 2000);

        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        // HttpEntity에 헤더와 바디 설정
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // OpenAI API 호출
        String url = "https://api.openai.com/v1/chat/completions";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.getBody());

        JsonNode contentNode = rootNode.path("choices").get(0).path("message").path("content");

        Pattern pattern = Pattern.compile("```json\\n(.*?)\\n```", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(contentNode.asText());
        String cleanJson="";
        if (matcher.find()) {
            String jsonBlock = matcher.group(1);
            cleanJson = jsonBlock.replaceAll("\\n", "").replaceAll("\\\\","").trim();
        } else {
            System.out.println("JSON 부분을 찾을 수 없습니다.");
        }
        return objectMapper.readValue(cleanJson, new TypeReference<Map<String, Object>>() {});
    }
}
