package by.akimova.aws.image;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource getDataSource() {
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.jdbc.Driver")
                .url("jdbc:mysql://image.ci4xtsh2mdnf.eu-central-1.rds.amazonaws.com/images")
                .username("root")
                .password("12345678")
                .build();
    }
}
