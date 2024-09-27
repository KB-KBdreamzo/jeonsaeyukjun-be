package com.jeonsaeyukjun.jeonsaeyukjunbe.report.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
public class OpenApiService {

    public Long getNowPrice(String jbAddress, Long legalCode, String buildingType, double buildingArea) {
        Long nowPrice = -1L;

        try {
            // 빌딩 타입에 맞게 매칭 (아파트,오피스텔만 가능 -> 개선 필요)
            int type = buildingType.equals("apartment") ? 1 : 2;

            String apiUrl = "https://api.kbland.kr/land-price/price/fastPriceInfo?%EB%B2%95%EC%A0%95%EB%8F%99%EC%BD%94%EB%93%9C="
                    + URLEncoder.encode(String.valueOf(legalCode), "UTF-8")
                    + "&%EC%9C%A0%ED%98%95=" + URLEncoder.encode(String.valueOf(type), "UTF-8")
                    + "&%EA%B1%B0%EB%9E%98%EC%9C%A0%ED%98%95=1"
                    + "&%EB%8B%A8%EC%A7%80%EA%B8%B0%EB%B3%B8%EC%9D%BC%EB%A0%A8%EB%B2%88%ED%98%B8=";

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                JSONArray dataArray = getResponse(connection);

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject dataObject = dataArray.getJSONObject(i);
                    String apiAddress = dataObject.getString("주소");

                    if (apiAddress.equals(jbAddress)) {
                        JSONArray saleArray = dataObject.getJSONArray("매매");

                        for (int j = 0; j < saleArray.length(); j++) {
                            JSONObject saleObject = saleArray.getJSONObject(j);
                            double contractArea = saleArray.getJSONObject(j).getDouble("전용면적");
                            if (contractArea == buildingArea) {
                                return saleObject.getLong("일반평균") * 10000;
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

    private static JSONArray getResponse(HttpURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONObject dataBody = jsonResponse.getJSONObject("dataBody");
        JSONArray dataArray = dataBody.getJSONArray("data");
        return dataArray;
    }
}