package com.shiro.entity;

import com.shiro.bean.JurRes;

import java.util.List;

/**
 * 资源查询
 * Created by 白超 on 2018/6/13.
 */
public class JurResGet extends JurRes {

    /**
     * 是否可以编辑
     */
    private boolean canEdit = true;

    private List<JurResGet> children;

    private Long childCount;

    private String parentResourceName;

    private boolean open;

    public String getLocationName() {
        return this.getName() + " (" + this.getParentResourceName() + ")";
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public List<JurResGet> getChildren() {
        return children;
    }

    public void setChildren(List<JurResGet> children) {
        this.children = children;
    }

    public Long getChildCount() {
        return childCount;
    }

    public void setChildCount(Long childCount) {
        this.childCount = childCount;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getParentResourceName() {
        return parentResourceName;
    }

    public void setParentResourceName(String parentResourceName) {
        this.parentResourceName = parentResourceName;
    }
}
