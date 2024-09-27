package com.jeonsaeyukjun.jeonsaeyukjunbe.report.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class CrawlingService {
        // WebDriver를 한번만 생성하고 재사용
        private WebDriver driver;

        // 생성자에서 WebDriver 초기화
        public CrawlingService() {
            System.setProperty("webdriver.chrome.driver", "/Users/yundabin/Downloads/chromedriver-mac-x64/chromedriver");
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--remote-allow-origins=*");
            chromeOptions.addArguments("--headless");
            driver = new ChromeDriver(chromeOptions);
        }

        public double getSalePriceRatio(String address, String buildingType) {
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

                WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@alt='검색']")));
                searchButton.click();

                if ("apartment".equalsIgnoreCase(buildingType)) {
                    WebElement apartmentPriceRatio = driver.findElement(By.xpath("//table//tr[td[contains(text(),'아파트')]]/td[last()]"));
                    String apartmentRatioText = apartmentPriceRatio.getText().replace("%", "");
                    salePriceRatio = Double.parseDouble(apartmentRatioText.trim());
                    System.out.println("아파트 매각가율: " + salePriceRatio);
                } else {
                    WebElement officetelPriceRatio = driver.findElement(By.xpath("//table//tr[td[contains(text(),'오피스텔')]]/td[last()]"));
                    String officetelRatioText = officetelPriceRatio.getText().replace("%", "");
                    salePriceRatio = Double.parseDouble(officetelRatioText.trim());
                    System.out.println("오피스텔 매각가율: " + salePriceRatio);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return salePriceRatio;
        }

        public boolean getHighTaxDelinquent(String name, String address) {
            boolean flag = false;

            List<String[]> results = new ArrayList<>();

            try {
                driver.get("https://www.nts.go.kr/nts/ad/openInfo/selectList.do?tcd=2");

                WebElement searchTypeDropdown = driver.findElement(By.name("searchType"));
                Select searchTypeSelect = new Select(searchTypeDropdown);
                searchTypeSelect.selectByVisibleText("성명");

                WebElement searchBox = driver.findElement(By.name("searchValue"));
                searchBox.sendKeys(name);

                WebElement searchButton = driver.findElement(By.className("btnSearch"));
                searchButton.click();

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
                WebElement resultsTable = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("tbody")));
                List<WebElement> rows = resultsTable.findElements(By.tagName("tr"));

                for (WebElement row : rows) {
                    List<WebElement> columns = row.findElements(By.tagName("td"));
                    if (columns.size() == 1) break;
                    String[] nameAndAddress = new String[2];
                    nameAndAddress[0] = columns.get(2).getText();
                    nameAndAddress[1] = columns.get(6).getText();
                    results.add(nameAndAddress);
                }

                if (!results.isEmpty()) flag = true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return flag;
        }

        public boolean getRentalFraud(String name, String address) {
            boolean flag = false;
            List<String[]> results = new ArrayList<>();

            try {
                driver.get("https://www.khug.or.kr/jeonse/web/s01/s010321.jsp?cur_page=");

                WebElement searchBox = driver.findElement(By.name("CUST_NM"));
                searchBox.sendKeys(name);

                WebElement searchButton = driver.findElement(By.id("bt1"));
                searchButton.click();

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
                WebElement resultsTable = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("tbody")));
                List<WebElement> rows = resultsTable.findElements(By.tagName("tr"));

                for (WebElement row : rows) {
                    List<WebElement> columns = row.findElements(By.tagName("td"));

                    String[] nameAndAddress = new String[2];
                    nameAndAddress[0] = columns.get(0).getText();
                    nameAndAddress[1] = columns.get(2).getText();
                    results.add(nameAndAddress);
                }

                if (!results.isEmpty()) flag = true;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return flag;
        }

       // 다쓰면 닫아야할 거 같은데 오류남 <- 처리 필요
    }
