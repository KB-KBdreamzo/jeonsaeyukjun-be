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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CrawlingService {
        private WebDriver driver;

    private static final Map<String, String> BUILDING_TYPE_MAP = Map.of(
            "아파트", "아파트",
            "단독주택", "단독주택",
            "다가구주택", "다가구주택",
            "연립주택", "연립주택",
            "다세대", "다세대",
            "상가", "상가",
            "오피스텔", "오피스텔",
            "근린시설", "근린시설"
    );

        public CrawlingService() {
            // 드라이버 맞게 고쳐야함
            System.setProperty("webdriver.chrome.driver", "/Users/yundabin/Downloads/chromedriver-mac-x64/chromedriver");
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--remote-allow-origins=*");
            chromeOptions.addArguments("--headless");
            driver = new ChromeDriver(chromeOptions);
        }

        public double getSalePriceRatio(String address, String buildingType) {
            double ratio = -1.0;

            try {
                driver.get("https://www.courtauction.go.kr/RealUtilMaeTong.laf");

                WebElement locationRadioButton = driver.findElement(By.id("idBubwLocGubun2"));
                locationRadioButton.click();

                String city = address.split(" ")[0];
                String district = address.split(" ")[1];

                selectDropdownOption(By.id("idSidoCode"), city);
                selectDropdownOption(By.id("idSiguCode"), district);
                WebElement searchButton = driver.findElement(By.xpath("//img[@alt='검색']"));
                searchButton.click();

                String type = BUILDING_TYPE_MAP.keySet().stream()
                        .filter(buildingType::contains)
                        .findFirst()
                        .map(BUILDING_TYPE_MAP::get)
                        .orElse("?");
                if (type.equals("?")) return ratio;

                return extractPriceRatio(type);

            } catch (Exception e) {
                e.printStackTrace();
                return ratio;
            }
        }

        private void selectDropdownOption(By selector, String visibleText) {
            WebElement searchTypeDropdown = driver.findElement(selector);
            Select searchTypeSelect = new Select(searchTypeDropdown);
            searchTypeSelect.selectByVisibleText(visibleText);
        }

        private double extractPriceRatio(String type) {
            WebElement resultsTable = driver.findElement(By.tagName("tbody"));
            List<WebElement> rows = resultsTable.findElements(By.tagName("tr"));

            for (WebElement row : rows) {
                List<WebElement> tds = row.findElements(By.tagName("td"));
                int tdCount = tds.size();

                String targetType = (tdCount == 7) ? tds.get(0).getText() : tds.get(1).getText();
                if (targetType.equals(type)) {
                    return Double.parseDouble(tds.get(tdCount-1).getText().replace("%", "").trim());
                }
            }
            return -1.0;
        }

        public boolean getHighTaxDelinquent(String name, String birth) {
            boolean flag = false;

            List<String[]> results = new ArrayList<>();

            try {
                driver.get("https://www.nts.go.kr/nts/ad/openInfo/selectList.do?tcd=2");

                selectDropdownOption(By.name("searchType"), "성명");

                WebElement searchBox = driver.findElement(By.name("searchValue"));
                searchBox.sendKeys(name);

                WebElement searchButton = driver.findElement(By.className("btnSearch"));
                searchButton.click();

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

                WebElement resultsTable = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("tbody")));
                List<WebElement> rows = resultsTable.findElements(By.tagName("tr"));

                // 이름 나이 날짜
                for (WebElement row : rows) {
                    List<WebElement> columns = row.findElements(By.tagName("td"));
                    if (columns.size() == 1) break;

                    int goal = Integer.parseInt(columns.get(3).getText());
                    int year = Integer.parseInt(columns.get(1).getText());
                    int age = getAge(birth, year);

                    if(age == goal) return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return flag;
        }

        public boolean getRentalFraud(String name, String birth) {
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
                    if (columns.size() == 1) break;

                    int goal = Integer.parseInt(columns.get(1).getText());
                    int year = Integer.parseInt(columns.get(9).getText().split("-")[0]);
                    int age = getAge(birth, year);

                    if(age == goal) return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return flag;
        }

        private static int getAge(String birth, int year) {
            int currentYear = LocalDate.now().getYear();
            int yearPart = Integer.parseInt(birth.substring(0, 2));
            String birthYear = (yearPart <= (currentYear % 100)) ? "20" + yearPart : "19" + yearPart;
            return year - Integer.parseInt(birthYear) - 1;
        }

       // 다쓰면 닫아야할 거 같은데 오류남 <- 처리 필요
    }
