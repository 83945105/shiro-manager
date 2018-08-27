package com.shiro.conf;

import pub.avalon.beans.EnumMethods;

/**
 * 字典
 * Created by 白超 on 2018/6/13.
 */
public class Dict {

    /**
     * 角色类型
     */
    public enum RoleType implements EnumMethods {

        LOCAL("本地角色"),
        OTHER("其它");

        public String disc;

        RoleType(String disc) {
            this.disc = disc;
        }

        @Override
        public Object getValue() {
            return this.name();
        }
    }

    /**
     * 资源类型
     */
    public enum ResourceType implements EnumMethods {
        URL("统一资源定位符"),
        PERMISSION("资源许可"),
        NODE("节点");

        public String disc;

        ResourceType(String disc) {
            this.disc = disc;
        }

        @Override
        public Object getValue() {
            return this.name();
        }
    }

    /**
     * 角色用户类型
     */
    public enum RoleUserType implements EnumMethods {

        LOCAL("本地角色"),
        OTHER("其它");

        public String disc;

        RoleUserType(String disc) {
            this.disc = disc;
        }

        @Override
        public Object getValue() {
            return this.name();
        }
    }
}
