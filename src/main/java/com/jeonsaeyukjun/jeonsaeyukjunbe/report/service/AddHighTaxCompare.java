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

public class AddHighTaxCompare {
    private static final Logger logger = LogManager.getLogger(AddHighTaxCompare.class);

    public static List<String> getAllTaxDelinquentNames() {
        System.setProperty("webdriver.chrome.driver", "/Users/jangbongjun/Desktop/KbDreamZo/jeonsaeyukjun-be/src/main/java/com/jeonsaeyukjun/jeonsaeyukjunbe/report/chromedriver"); // ChromeDriver의 경로를 수정
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.addArguments("--headless");
        WebDriver driver = new ChromeDriver(chromeOptions);
        List<String> allNames = new ArrayList<>();

        try {

            String baseUrl = "https://www.nts.go.kr/nts/ad/openInfo/selectList.do";
            driver.get(baseUrl);

            while (true) {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tr")));

                List<WebElement> rows = driver.findElements(By.cssSelector("tr"));
                allNames.addAll(
                        rows.stream()
                                .map(row -> row.findElements(By.tagName("td")))
                                .filter(columns -> columns.size() > 4)
                                .map(columns -> columns.get(3).getText())
                                .collect(Collectors.toList())
                );

                System.out.println("페이지 완료: " + driver.getCurrentUrl());


                List<WebElement> nextButtons = driver.findElements(By.cssSelector("a.bbs_arr.pgeR1"));
                if (nextButtons.isEmpty()) {
                    break;
                }


                WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(nextButtons.get(0)));
                nextButton.click();


                wait.until(ExpectedConditions.stalenessOf(rows.get(0)));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tr")));
            }

        } catch (Exception e) {
            logger.error("Error while crawling tax delinquent names: ", e);
        } finally {
            driver.quit();
        }

        return allNames;
    }

    public static void main(String[] args) {

        List<String> taxDelinquentNames = getAllTaxDelinquentNames();
        for (String taxDelinquentName : taxDelinquentNames) {
            System.out.println("taxDelinquentName = " + taxDelinquentName);
        }
    }
}
