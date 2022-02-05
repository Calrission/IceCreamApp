package com.ege.icecreamapp

data class ModelIceCream(
    val title: String,
    val desc: String,
    val edition: String,
    val price: Float,
    val imgRes: Int,
    var countBuy: Int = 1,
    var size: Int = 2,
    // Пересоздание объектов Setting для разрыва ссылок на элементы списка settings в GroupSetting
    var groupSetting: List<GroupSetting> = StaticData.groups.map { GroupSetting(it.title, it.settings.map {setting -> Setting(setting.name, setting.isActivation, setting.cost) }, it.canMultiSelect) }
)

data class GroupSetting(
    val title: String,
    val settings: List<Setting>,
    val canMultiSelect: Boolean = false,
)

data class Setting (
    val name: String,
    var isActivation: Boolean,
    val cost: Float = 0f
)

