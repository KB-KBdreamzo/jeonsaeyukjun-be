package com.jeonsaeyukjun.jeonsaeyukjunbe.report.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
public class OpenApiService {

    public Long getNowPrice(String jbAddress, String legalCode, String buildingType, double buildingArea) {
        Long nowPrice = -1L;

        try {
            // 빌딩 타입에 맞게 매칭 (아파트,오피스텔만 가능 -> 개선 필요)
            int type = buildingType.equals("apartment") ? 1 : 2;

            // 인코딩 때문인거 같은데yo.. 해결 부탁
            String apiUrl = "https://api.kbland.kr/land-price/price/fastPriceInfo?%EB%B2%95%EC%A0%95%EB%8F%99%EC%BD%94%EB%93%9C="
                    + URLEncoder.encode(legalCode, "UTF-8")
                    + "&%EC%9C%A0%ED%98%95=" + URLEncoder.encode(String.valueOf(type), "UTF-8")
                    + "&%EA%B1%B0%EB%9E%98%EC%9C%A0%ED%98%95=1"
                    + "&%EB%8B%A8%EC%A7%80%EA%B8%B0%EB%B3%B8%EC%9D%BC%EB%A0%A8%EB%B2%88%ED%98%B8=";

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                JsonNode dataArray = getResponse(connection);

                for (JsonNode dataObject : dataArray) {
                    String apiAddress = dataObject.get("주소").asText();

                    if (apiAddress.equals(jbAddress)) {
                        JsonNode saleArray = dataObject.get("매매");

                        for (JsonNode saleObject : saleArray) {
                            double contractArea = saleObject.get("전용면적").asDouble();
                            if (contractArea == buildingArea) {
                                return saleObject.get("일반평균").asLong() * 10000;
                            }
                        }
                    }
                }
            } else {
                System.out.println("API 요청 실패. 응답 코드: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return nowPrice;
    }

    private static JsonNode getResponse(HttpURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(response.toString());
        return jsonResponse.get("dataBody").get("data");
    }
}
