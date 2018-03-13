package com.qingmei2.rximagepicker.entity;

import com.qingmei2.rximagepicker.ui.ICustomPickerConfiguration;

import java.util.Map;

public final class CustomPickConfigurations {

    private Map<String, ICustomPickerConfiguration> configurations;

    public CustomPickConfigurations(Map<String, ICustomPickerConfiguration> configurations) {
        if (configurations == null)
            throw new NullPointerException("configurations is null.");
        this.configurations = configurations;
    }

    public void resetConfigrations(Map<String, ICustomPickerConfiguration> configurations) {
        this.configurations = configurations;
    }

    public ICustomPickerConfiguration findConfigurationByKey(String viewKey) {
        if (viewKey == null)
            throw new NullPointerException("viewKey is null");
        return configurations.get(viewKey);
    }
}
