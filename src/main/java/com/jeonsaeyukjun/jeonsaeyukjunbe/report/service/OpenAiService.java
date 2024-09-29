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

        List<Map<String, String>> messages = List.of(
                Map.of("role", "system", "content", "You are a helpful assistant."),
                Map.of("role", "user", "content", createRequest(type) + "\n" + text)
        );
        HttpHeaders headers = createHeaders();
        Map<String, Object> requestBody = createBody(messages);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        String url = "https://api.openai.com/v1/chat/completions";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        return extractStringToJson(response.getBody());
    }

    private static String createRequest(String type) {
        return switch (type){
            case "표제부" -> """
                    roadName(도로명주소를 찾아서 String)\
                    detailAddress (상세주소는 [집합 건물] 옆에 쓰인 내용 전체를 (예로 #층 #동 #호같은 내용) 찾아서 String
                    buildingType(건물용도는 건물 내역에 부분 값을 아파트, 단독주택, 다세대주택, 연립주택, 다세대, 상가, 오피스텔, 기타 로 나눠서 반환해줘 예로 단지형 다세대면 다세대주택이야String)
                    landType (토지 지목을 찾아서 String)
                    landArea(토지 면적을 찾아서 Double)
                    buildingArea (1동의 건물의 표시의 건물 내역을 찾고 거기 나온 면적 중 최댓값을 찾아서 Double)
                    area (용면적 즉 전유부분의건물의표시 부분의 건물 내역에 쓰인 면적을 찾아서 Double)
                    위의 각 요소들을 찾고 json 형태로 반환해줘, 이때 키값은 왼쪽의 영어이름이고 값은 오른쪽 괄호안의 자료형에 맞게 값을 넣어줘
                    """;
            case "갑구" -> """
                    lessorName 소유자 이름 (마지막 소유권 이전의 소유자 이름을 찾아서 String)\
                    lessorBirth (위의 소유자에 나와있는 생년월일을 찾아서 String)
                    auctionRecord(등기 목적에서 경매개시결정을 찾아서 있는지 없는지를 찾아서 boolean)
                    injuctionRecord(등기 목적에서 가처분이 있는지 없는지를 찾아서 boolean. 단 경매개시결정 이후에 있다면 없는거야)
                    trustRegistrationRecord (신탁이 있는지 없는지를 찾아서 boolean)
                    redemptionRecord(환매특약의 여부를 찾아서 boolean)
                    registrationRecord (등기 목적에서 가등기가 있는지 없는지를 찾아서 boolean. 단 경매개시결정 이후에 있다면 없는거야)
                    seizureCount(등기 목적에서 압류의 횟수를 찾아서 Integer. 말소가 있다면 해당 압류는 없는거야)
                    provisionalSeizureCount(등기 목적에서 가압류의 횟수를 찾아서 Integer. 말소가 있다면 해당 가압류는 없는거야)\
                    위의 각 요소들을 찾고 json 형태로 반환해줘, 이때 키값은 왼쪽의 영어이름이고 값은 오른쪽 괄호안의 자료형에 맞게 값을 넣어줘""";
            case "을구" -> """
                    priorityDeposit (선순위 채권 총액 Long)
                    mortgageCount(근저당권 설정 내역 부분을 찾아서 Integer, 단 해당하는 설정의 말소가 있다면 세지 말아줘)
                    leaseholdRegistrationCount(전세권 설정 내역을 찾아서 Integer, 단 해당하는 설정의 말소가 있다면 세지 말아줘)\
                    위의 각 요소들을 찾고 json 형태로 반환해줘, 이때 키값은 왼쪽의 영어이름이고 값은 오른쪽 괄호안의 자료형에 맞게 값을 넣어줘""";

            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    private static Map<String, Object> createBody(List<Map<String, String>> messages) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o-mini");
        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 2000);
        return requestBody;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");
        return headers;
    }

    private Map<String, Object> extractStringToJson(String responseBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(responseBody);
        String content = rootNode.path("choices").get(0).path("message").path("content").asText();

        Matcher matcher = Pattern.compile("```json\\n(.*?)\\n```", Pattern.DOTALL).matcher(content);
        if (matcher.find()) {
            String cleanJson = matcher.group(1).replaceAll("\\n", "").replaceAll("\\\\", "").trim();
            return objectMapper.readValue(cleanJson, new TypeReference<>() {});
        } else {
            throw new IOException("JSON 부분을 찾을 수 없습니다.");
        }
    }
}
