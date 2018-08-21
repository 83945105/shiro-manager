package com.shiro.norm;

/**
 * Created by 白超 on 2018/6/21.
 */
public interface ShiroRole {

    /**
     * 获取ID
     *
     * @return
     */
    String getId();

    /**
     * 获取角色名
     *
     * @return
     */
    String getName();

    /**
     * 获取角色值
     *
     * @return
     */
    String getRole();

    /**
     * 获取角色类型
     *
     * @return
     */
    String getType();

    /**
     * 获取角色状态
     *
     * @return
     */
    String getStatus();
}
