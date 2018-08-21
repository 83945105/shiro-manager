package com.shiro.entity;

import com.shiro.bean.JurRes;

/**
 * 资源提交
 * Created by 白超 on 2018/6/13.
 */
public class JurResPost extends JurRes {

    /**
     * 使用该资源的角色ID
     */
    private String[] roleIds;

    private String[] newParentIds;

    private Integer limitStart;

    private Integer limitEnd;

    private String searchText;

    private String[] searchTypes;

    public String[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String[] roleIds) {
        this.roleIds = roleIds;
    }

    public String[] getNewParentIds() {
        return newParentIds;
    }

    public void setNewParentIds(String[] newParentIds) {
        this.newParentIds = newParentIds;
    }

    public Integer getLimitStart() {
        return limitStart;
    }

    public void setLimitStart(Integer limitStart) {
        this.limitStart = limitStart;
    }

    public Integer getLimitEnd() {
        return limitEnd;
    }

    public void setLimitEnd(Integer limitEnd) {
        this.limitEnd = limitEnd;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String[] getSearchTypes() {
        return searchTypes;
    }

    public void setSearchTypes(String[] searchTypes) {
        this.searchTypes = searchTypes;
    }
}
