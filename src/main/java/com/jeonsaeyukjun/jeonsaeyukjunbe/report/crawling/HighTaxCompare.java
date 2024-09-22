package com.jeonsaeyukjun.jeonsaeyukjunbe.report.crawling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HighTaxCompare {

    // 이름 데이터를 가져오는 메서드
    public static List<String> getTaxDelinquentNamesFromPages(int totalPages) {
        List<String> allNames = new ArrayList<>();

        try {
            // 페이지 번호를 순회하며 각 페이지에서 데이터를 가져옴
            for (int page = 1; page <= totalPages; page++) {
                // 페이지 번호가 포함된 URL로 접속
                String url = "https://www.nts.go.kr/nts/ad/openInfo/selectList.do?tcd=2&othbcYear=2023&page=" + page;
                Document doc = Jsoup.connect(url).get();

                // <tr> 요소들을 선택
                Elements rows = doc.select("tr");

                // 각 <tr> 안에서 두 번째 <td> (이름이 있는 곳)를 선택
                for (Element row : rows) {
                    Elements columns = row.select("td"); // 모든 <td> 선택
                    if (columns.size() > 1) { // 두 번째 <td>가 있는지 확인
                        String name = columns.get(1).text(); // 두 번째 <td> (이름)
                        allNames.add(name);
                    }
                }

                System.out.println("Page " + page + " 완료"); // 각 페이지가 완료됐음을 출력
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return allNames;
    }


    public static void main(String[] args) {
        int totalPages = 5;

        // 여러 페이지에서 크롤링한 이름을 가져옴
        List<String> taxDelinquentNames = getTaxDelinquentNamesFromPages(totalPages);

        // 크롤링한 이름을 출력
        for (String name : taxDelinquentNames) {
            System.out.println("Tax Delinquent Name: " + name);
        }
    }
}
//        List<String> taxDelinquents = getTaxDelinquentNames();
//
//        // 크롤링한 데이터의 크기 출력
//        System.out.println("크롤링한 데이터 수: " + taxDelinquents.size());
//
//        // 크롤링한 데이터가 있으면 출력
//        if (taxDelinquents.isEmpty()) {
//            System.out.println("크롤링된 데이터가 없습니다.");
//        } else {
//            for (String name : taxDelinquents) {
//                System.out.println("Tax Delinquent: " + name);
//            }
//        }
//        // 1. DB에서 임대인 이름 목록을 가져옴
//        List<String> dbNames = DatabaseHelper.getNamesFromDatabase();
//
//        // 2. 크롤링을 통해 체납자 목록 가져옴
//        List<String> taxDelinquents = getTaxDelinquentNames();
//
//        // 3. 체납자 목록과 DB 임대인 이름 비교
//        for (String dbName : dbNames) {
//            if (taxDelinquents.contains(dbName)) {
//                System.out.println(dbName + "는 체납자 목록에 있음");
//            } else {
//                System.out.println(dbName + "는 체납자 목록에 없음");
//            }
//        }
//        for (String name : taxDelinquents) {
//            System.out.println("Tax Delinquent: " + name);
//        }


