package com.shiro.norm;

/**
 * Created by 白超 on 2018/6/7.
 */
public interface ShiroUser extends Certificate {

    /**
     * 获取用户ID
     *
     * @return
     */
    String getId();
}
