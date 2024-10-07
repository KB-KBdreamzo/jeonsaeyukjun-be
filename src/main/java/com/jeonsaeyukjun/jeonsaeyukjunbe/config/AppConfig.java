package com.jeonsaeyukjun.jeonsaeyukjunbe.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@MapperScan(basePackages = {"com.jeonsaeyukjun.jeonsaeyukjunbe.**.mapper"})
@PropertySource("classpath:application.properties") // 프로퍼티 파일을 명시적으로 로드
//@ComponentScan(basePackages = {"com.jeonsaeyukjun.jeonsaeyukjunbe.**.mapper"})
public class AppConfig {

    // Environment 객체 주입
    @Autowired
    private Environment env;

//    @Value("${db.url}")
//    private String jdbcUrl;
//
//    @Value("${db.username}")
//    private String username;
//
//    @Value("${db.password}")
//    private String password;

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource= new HikariDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // 환경변수에서 JDBC URL, 사용자명, 비밀번호를 가져오는 부분
        String jdbcUrl = env.getProperty("db.url");
        String username = env.getProperty("db.username");
        String password = env.getProperty("db.password");

        // 값을 출력하여 확인
        System.out.println("jdbcUrl: " + jdbcUrl);
        System.out.println("username: " + username);
        System.out.println("password: " + password);

        if (jdbcUrl == null || username == null || password == null) {
            throw new IllegalArgumentException("JDBC URL, username, and password must not be null.");
        }
//        // Environment로 프로퍼티 값 가져오기
//        dataSource.setJdbcUrl(env.getProperty("db.url"));
//        dataSource.setUsername(env.getProperty("db.username"));
//        dataSource.setPassword(env.getProperty("db.password"));

        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setAutoCommit(true);
        return dataSource;
    }

    @Bean public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Autowired
    ApplicationContext applicationContext;

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws IOException {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setConfigLocation(applicationContext.getResource("classpath:/mybatis/mybatis-config.xml"));
        factoryBean.setMapperLocations(applicationContext.getResources("classpath:/mybatis/mappers/*.xml"));
        return factoryBean;
    }

    @Bean
    public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(10485760); // 10MB
        multipartResolver.setDefaultEncoding("UTF-8");
        return multipartResolver;
    }
}