package com.global.conf;

import com.dt.jdbc.core.SpringJdbcEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author 白超
 * @version 1.0
 * @see
 * @since 2018/7/11
 */
@Configuration
public class JdbcConfig {

    @Bean
    public SpringJdbcEngine springJdbcEngine(JdbcTemplate jdbcTemplate) {
        SpringJdbcEngine engine = new SpringJdbcEngine();
        engine.setJdbcTemplate(jdbcTemplate);
        return engine;
    }
}
