package com.shiro.modules;

import java.util.ArrayList;
import java.util.List;

/**
 * Shiro模块
 * Created by 白超 on 2018/6/13.
 */
public class ShiroModules {

    private List<Module> modules = new ArrayList<>();

    /**
     * 添加模块
     *
     * @param module
     * @return
     */
    public ShiroModules addModule(Module module) {
        this.modules.add(module);
        return this;
    }

    /**
     * 获取指定模块
     *
     * @param clazz
     * @return
     */
    public Module getModule(Class<? extends Module> clazz) {
        for (Module module : modules) {
            if (clazz.isAssignableFrom(module.getClass())) {
                return module;
            }
        }
        return null;
    }
}
