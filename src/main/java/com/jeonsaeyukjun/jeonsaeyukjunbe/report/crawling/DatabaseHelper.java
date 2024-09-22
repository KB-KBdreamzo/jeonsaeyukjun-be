package com.jeonsaeyukjun.jeonsaeyukjunbe.report.crawling;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    // 데이터베이스 연결 정보
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/mydatabase"; // your DB URL
    private static final String USERNAME = "root"; // your DB username
    private static final String PASSWORD = ""; // your DB password

    // DB에서 임대인 이름 목록을 가져오는 메서드
    public static List<String> getNamesFromDatabase() {
        List<String> names = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {

            String query = "SELECT `임대인 이름` FROM 리포트"; // 리포트 테이블에서 임대인 이름 가져옴
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                names.add(rs.getString("임대인 이름"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return names;
    }
}
