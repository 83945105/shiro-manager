package com.shiro.model;

import com.dt.core.converter.HumpConverter;
import com.dt.core.jdbc.JdbcSourceEngine;
import com.dt.core.model.ModelTemplateEngine;

import java.sql.SQLException;

/**
 * @author 白超
 * @version 1.0
 * @see
 * @since 2018/7/10
 */
public class Generate {

    public static void main(String[] args) throws SQLException {
        JdbcSourceEngine engine = JdbcSourceEngine.newMySqlEngine("192.168.0.112",
                "3306", "shiro-manager-spring", "root", "root");

        new ModelTemplateEngine(engine, new HumpConverter())
//                .addTable("jur_res", "JurRes")
//                .addTable("jur_role", "JurRole")
                .addTable("jur_role_res", "JurRoleRes")
                .addTable("jur_role_user", "JurRoleUser")
//                .addTable("zuul_route", "ZuulRoute")
                .process("/", "com.base.model");
    }

}
