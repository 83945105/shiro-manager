package com.shiro.entity;

import com.shiro.bean.JurRole;

/**
 * @author 白超
 * @version 1.0
 * @see
 * @since 2018/7/12
 */
public class JurRolePost extends JurRole {

    private String searchText;

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}
