package com.unicorns.backend.config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Configuration
@PropertySource(value="classpath:backend-config.properties", encoding="UTF-8")
public class BackendConfig {

    @Value("${quiz.nickname-column-mapping}")
    private String mapColumnNickname;
    @Value("${quiz.total-points-column-mapping}")
    private String mapColumnTotalPoints;
    @Value("${quiz.time-start-column-mapping}")
    private String mapColumnTimeStart;
    @Value("${quiz.time-end-column-mapping}")
    private String mapColumnTimeEnd;
    @Value("${quiz.preferences-column-mapping}")
    private String mapColumnPreferences;
    @Value("${upload-required-file-extension}")
    private String uploadRequiredFileExtension;
    @Value("${quiz.question-columns-range}")
    private int questionColumnsRange;
    @Value("${quiz.questions-before-column-mapping}")
    private String questionsBeforeColumnMapping;
    @Value("${quiz.questions-after-column-mapping}")
    private String questionsAfterColumnMapping;
}

//@Getter
//@Configuration
//@PropertySource("classpath:backend-config.properties")
//public class BackendConfig {
//    @Bean
//    public PropertiesFactoryBean propertiesFileMapping() {
//        PropertiesFactoryBean factoryBean = new PropertiesFactoryBean();
//        factoryBean.setFileEncoding("UTF-8");
//        factoryBean.setLocation(new ClassPathResource("backend-config.properties"));
//        return factoryBean;
//    }
//
//    @Value("#{propertiesFileMapping['map.column-nickname']}")
//    private String mapColumnNickname;
//    @Value("#{propertiesFileMapping['map.column-total-points']}")
//    private String mapColumnTotalPoints;
//    @Value("#{propertiesFileMapping['map.column-time-start']}")
//    private String mapColumnTimeStart;
//    @Value("#{propertiesFileMapping['map.column-time-end']}")
//    private String mapColumnTimeEnd;
//    @Value("#{propertiesFileMapping['map.column-preferences']}")
//    private String mapColumnPreferences;
//    @Value("#{propertiesFileMapping['upload-required-file-extension']}")
//    private String uploadRequiredFileExtension;
//}
