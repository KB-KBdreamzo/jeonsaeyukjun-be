package com.jeonsaeyukjun.jeonsaeyukjunbe.report.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class AddJeonsaeFraud {

    private static final Logger logger = LogManager.getLogger(AddJeonsaeFraud.class);

    public static List<String> getAllTaxDelinquentNamesAndAddresses() {
        System.setProperty("webdriver.chrome.driver", "/Users/jangbongjun/Desktop/KbDreamZo/jeonsaeyukjun-be/src/main/java/com/jeonsaeyukjun/jeonsaeyukjunbe/report/chromedriver");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.addArguments("--headless");
        WebDriver driver = new ChromeDriver(chromeOptions);
        List<String> allDetails = new ArrayList<>();

        try {
            int curPage = 1;
            String baseUrl = "https://www.khug.or.kr/jeonse/web/s01/s010321.jsp?cur_page=";

            while (curPage <= 80) {
                String url = baseUrl + curPage;
                System.out.println("Loading URL: " + url);
                driver.get(url);

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tr")));

                List<WebElement> rows = driver.findElements(By.cssSelector("tr"));
                System.out.println("페이지에서 로드된 row 수: " + rows.size());

                if (rows.isEmpty()) {
                    System.out.println("페이지에 데이터가 없습니다. 크롤링을 종료합니다.");
                    break;
                }

                allDetails.addAll(
                        rows.stream()
                                .map(row -> row.findElements(By.tagName("td")))
                                .filter(columns -> columns.size() > 1)
                                .map(columns -> {
                                    String name = columns.get(0).getText();
                                    String address = columns.get(2).getText();
                                    String detail = "이름: " + name + ", 주소: " + address;
                                    System.out.println("추출된 데이터: " + detail);
                                    return detail;
                                })
                                .filter(detail -> !detail.trim().isEmpty())
                                .collect(Collectors.toList())
                );

                System.out.println("페이지 완료: " + driver.getCurrentUrl());

                curPage++;
            }

        } catch (Exception e) {
            logger.error("세금 체납자 이름 및 주소를 크롤링하는 중 오류 발생: ", e);
        } finally {
            driver.quit();
        }

        return allDetails;
    }

    public static void main(String[] args) {
        List<String> taxDelinquentDetails = getAllTaxDelinquentNamesAndAddresses();
        for (String detail : taxDelinquentDetails) {
            System.out.println("체납자 정보 = " + detail);
        }
    }
}
