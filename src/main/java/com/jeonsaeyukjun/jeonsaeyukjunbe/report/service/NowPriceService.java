package com.jeonsaeyukjun.jeonsaeyukjunbe.report.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class NowPriceService {

    public static Long getNowPrice(String legalCode, String buildingType, String address, double buildingArea) {
        Long nowPrice = 0L;
        try {
            int type = buildingType.equals("apartment") ? 1 : 2;
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
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray dataArray = jsonResponse.getJSONObject("dataBody").getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject complex = dataArray.getJSONObject(i);
                    String addressFromApi = complex.getString("주소");
                    JSONArray salesArray = complex.getJSONArray("매매");

                    for (int j = 0; j < salesArray.length(); j++) {
                        JSONObject sale = salesArray.getJSONObject(j);
                        double averagePrice = sale.getDouble("일반평균");

                        if (addressFromApi.equals(address) && buildingArea == sale.getDouble("전용면적")) {
                            nowPrice = (long) Math.floor(averagePrice * 10000);
                            break;
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
}