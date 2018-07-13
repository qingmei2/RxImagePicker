package com.qingmei2.rximagepicker.entity

import com.qingmei2.rximagepicker.ui.ICustomPickerConfiguration

class CustomPickConfigurations(private var configurations: Map<String, ICustomPickerConfiguration>) {

    fun resetConfigrations(configurations: Map<String, ICustomPickerConfiguration>) {
        this.configurations = configurations
    }

    fun findConfigurationByKey(viewKey: String): ICustomPickerConfiguration {
        return configurations[viewKey]!!
    }
}
