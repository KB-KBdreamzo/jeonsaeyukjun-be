package com.jeonsaeyukjun.jeonsaeyukjunbe.report.service;

import java.time.Duration;
import java.time.LocalDate;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

@Service
public class SalePriceRatioService {

    public double getSalePriceRatio(String address, String buildingType) {

        System.setProperty("webdriver.chrome.driver", "/Users/user/chromedriver-mac-arm64/chromedriver");

        WebDriver driver = new ChromeDriver();
        double salePriceRatio = 0.0;

        try {
            driver.get("https://www.courtauction.go.kr/RealUtilMaeTong.laf");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

            WebElement locationRadioButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("idBubwLocGubun2")));
            locationRadioButton.click();

            String city = address.split(" ")[0];
            String district = address.split(" ")[1];

            WebElement sidoSelectElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("idSidoCode")));
            Select sidoSelect = new Select(sidoSelectElement);
            sidoSelect.selectByVisibleText(city);

            WebElement siguSelectElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("idSiguCode")));
            Select siguSelect = new Select(siguSelectElement);
            siguSelect.selectByVisibleText(district);

            LocalDate today = LocalDate.now();
            LocalDate oneYearAgo = today.minusYears(1);

            int startYear = oneYearAgo.getYear();
            int startMonth = oneYearAgo.getMonthValue();
            int endYear = today.getYear();
            int endMonth = (today.getMonthValue() == 1) ? 12 : today.getMonthValue() - 1;

            WebElement startYearSelectElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("idSelStartYear")));
            Select startYearSelect = new Select(startYearSelectElement);
            startYearSelect.selectByVisibleText(String.valueOf(startYear));

            WebElement startMonthSelectElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("idSelStartMnth")));
            Select startMonthSelect = new Select(startMonthSelectElement);
            startMonthSelect.selectByVisibleText(String.valueOf(startMonth));

            WebElement endYearSelectElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("idSelEndYear")));
            Select endYearSelect = new Select(endYearSelectElement);
            endYearSelect.selectByVisibleText(String.valueOf(endYear));

            WebElement endMonthSelectElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("idSelEndMnth")));
            Select endMonthSelect = new Select(endMonthSelectElement);
            endMonthSelect.selectByVisibleText(String.valueOf(endMonth));

            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@alt='검색']")));
            searchButton.click();

            WebElement apartmentPriceRatio = driver.findElement(By.xpath("//table//tr[td[contains(text(),'아파트')]]/td[last()]"));
            WebElement officetelPriceRatio = driver.findElement(By.xpath("//table//tr[td[contains(text(),'오피스텔')]]/td[last()]"));

            String apartmentRatioText = apartmentPriceRatio.getText().replace("%", "");
            String officetelRatioText = officetelPriceRatio.getText().replace("%", "");

            if ("apartment".equalsIgnoreCase(buildingType)) {
                salePriceRatio = Double.parseDouble(apartmentRatioText.trim());
                System.out.println("아파트 매각가율: " + salePriceRatio);
            } else {
                salePriceRatio = Double.parseDouble(officetelRatioText.trim());
                System.out.println("오피스텔 매각가율: " + salePriceRatio);
            }

        } catch (NumberFormatException e) {
            System.out.println("매각가율 값을 숫자로 변환하는 중에 오류 발생: " + e.getMessage());
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            driver.quit();
        }

        return salePriceRatio;
    }
}
