package com.shiro.entity;

import com.shiro.bean.JurRole;

/**
 * @author 白超
 * @version 1.0
 * @see
 * @since 2018/7/12
 */
public class JurRoleGet extends JurRole {

    /**
     * 是否可以编辑
     */
    private boolean canEdit = true;

    /**
     * 是否可以改变状态
     */
    private boolean canChangeStatus = true;

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public boolean isCanChangeStatus() {
        return canChangeStatus;
    }

    public void setCanChangeStatus(boolean canChangeStatus) {
        this.canChangeStatus = canChangeStatus;
    }
}
