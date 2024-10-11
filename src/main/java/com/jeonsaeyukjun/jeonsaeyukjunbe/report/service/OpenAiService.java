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
                Map.of("role", "user", "content", text + "\n" +createRequest(type))
        );
        HttpHeaders headers = createHeaders();
        Map<String, Object> requestBody = createBody(messages);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        String url = "https://api.openai.com/v1/chat/completions";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        return extractStringToJson(response.getBody());
    }

    private static String createRequest(String type) {
        return switch (type) {
            case "표제부" -> """
                <여기까지가 내용이야>
                
                <요구사항>
                1. roadName(string [도로명주소]를 찾아서 그 밑으로 있는 내용 중 도로명 주소 찾아줘 형식은 시 군 구 로 숫자 이런 형식이야)
                2. buildingType(string 건물용도는 건물 내역에 부분 값을 아파트, 단독주택, 다세대주택, 연립주택, 다세대, 상가, 오피스텔, 기타로 나눠서 찾아줘. 예로 단지형 다세대면 다세대주택으로 지정 String)
                3. landType(string 토지 지목을 찾아줘)
                4. landArea(double 토지 면적을 찾아줘)
                5. buildingArea(double 1동의 건물의 표시의 건물 내역을 찾은뒤 그 면적 중 최댓값을 찾아줘)
                6. area(double 용면적, 즉 전유부분의 건물의 표시 부분의 건물 내역에 쓰인 면적을 찾아줘)
                7. isIllegalBuilding (boolean 불법건축물 이라는 글자가 있으면 true, 아니면 false)
                
                <금지사항>
                1. 잘못된 값은 넣지 말고 말소된 항목은 제외해줘
                2. 같은 줄에 띄어쓰기가 있다면 다른 내용이므로 조심해. 다른 줄의 경우엔 같은 내용일 수 있어
                   예로 서울특별시 강서구 허준로  4층
                       47  5층 
                   이라면 도로명주소는 서울특별시 강서구 허준로 47이 하나의 내용인거야
                3. roadname의 경우 꼭 뒤의 숫자형태도 포함해줘
                4. buildingType의 결과값은 꼭 아파트, 단독주택, 다세대주택, 연립주택, 다세대, 상가, 오피스텔, 기타중 하나여야해 명심해
                   예로 단지형 다세대 주택이라면 다세대주택인거야. 종류가 무조건 8개중 하나여야해
                5. 출력은 json 형식이며, 괄호안의 값이 키값이고 자료형 및 반환할 값이야.
                
                <요구사항>에 맞춰서 내용을 추출해줘. <금지사항>을 꼭 주의해서 지켜줘
                출력은 꼭 json형식으로 반환해줘. 한 줄 한 줄 천천히 읽고 생각하면서 답변해
                """;
            case "갑구" -> """
                <여기까지가 내용이야>
                
                <요구사항>
                1. lessorName(String 소유자 이름을 찾아줘. 단, 마지막 소유권 이전의 소유자를 기준으로 해줘)
                2. lessorBirth(String 소유자의 생년월일 맨앞 6자리 찾아줘)
                3. auctionRecord(boolean 경매개시결정이 있는지 여부를 찾아줘)
                4. injuctionRecord(boolean 가처분이 있는지 여부를 찾아줘)
                5. trustRegistrationRecord(boolean 신탁이 있는지 여부를 찾아줘)
                6. redemptionRecord(boolean 환매특약이 있는지 여부를 찾아줘)
                7. registrationRecord(boolean 가등기가 있는지 여부를 찾아줘.)
                8. seizureCount(integer 압류 횟수를 찾아줘.)
                9. provisionalSeizureCount(integer 가압류 횟수를 찾아줘.)
                
                <금지사항>
                1. 잘못된 값은 넣지 말고 말소된 항목은 제외해줘
                2. 같은 줄에 띄어쓰기가 있다면 다른 내용이야. 다른 줄의 경우엔 같은 내용일 수 있어
                3. 가처분, 가등기의 경우 경매 개시 결정 이후에 있다면 제외해줘.
                4. 압류, 가압류의 경우 말소된 건은 빼줘.
                5. 출력은 json 형식이며, 괄호안의 값이 키값이고 자료형 및 반환할 값이야.
                
                <요구사항>에 맞춰서 내용을 추출해줘. <금지사항>을 꼭 주의해서 지켜줘
                출력은 꼭 json형식으로 반환해줘. 한 줄 한 줄 천천히 읽고 생각하면서 답변해
                """;
            case "을구" -> """
                <여기까지가 내용이야>

                <요구사항>
                1. priorityDeposit(long 선순위 채권 총액을 찾아줘. 말소된 경우 금액도 같이 없어져. 잘 읽어보고 고려해)
                2. mortgageCount(integer 근저당권 설정 내역을 찾아줘 말소의 경우는 꼭 빼야해. 잘 읽어보고 고려해)
                3. leaseholdRegistrationCount(integer 전세권 설정 내역을 찾아줘. 잘 읽어보고 고려해)
                
                <금지사항>
                1. 잘못된 값은 넣지 말고 말소된 항목이 있다면, 이전에 있던 항목을 무효하라는 이야기야. 즉 설정을 빼라는 이야기지
                2. 같은 줄에 띄어쓰기가 있다면 다른 내용이므로 조심해. 다른 줄의 경우엔 같은 내용일 수 있어
                   예로 5 1번근저당권설정, 2009년2월19일 2009년2월19일
                         2번근저당권설정 제10785호 해지
                         등기말소
                   의 경우에는 1번 근저당권설정, 2번 근저당권설정 등기말소 인거야. 
                3. 근저당권이전과 전세권 이전은 횟수에 아예 반영하지 마
                4. 말소의 경우 , 가 있다면 한번에 여러건이 말소될 수 있으니 -2, -3이 될 수도 있으니 잘 분석해
                5. 출력은 json 형식이며, 괄호안의 값이 키값이고 자료형 및 반환할 값이야.
                
                <요구사항>에 맞춰서 내용을 추출해줘. <금지사항>을 꼭 주의해서 지켜줘
                출력은 꼭 json형식으로 반환해줘. 한 줄 한 줄 천천히 읽고 생각하면서 답변해
                여기에는 각각을 왜그렇게 판단했는지 이유도 적어봐
                """;

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
        System.out.println(content);
        Matcher matcher = Pattern.compile("```json\\n(.*?)\\n```", Pattern.DOTALL).matcher(content);
        if (matcher.find()) {
            String cleanJson = matcher.group(1).replaceAll("\\n", "").replaceAll("\\\\", "").trim();
            return objectMapper.readValue(cleanJson, new TypeReference<>() {});
        } else {
            throw new IOException("JSON 부분을 찾을 수 없습니다.");
        }
    }
}
