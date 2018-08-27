package com.shiro.entity;

import pub.avalon.holygrail.response.beans.Status;

/**
 * Created by 白超 on 2018/7/24.
 */
public interface JurUserGet {

    String getId();

    boolean disabled();

    default String getStatus() {
        if (this.disabled()) {
            return Status.DISABLED.name();
        }
        return Status.NORMAL.name();
    }
}
