package com.servegame.yeyyyyyy.luckywheel.menusystem

import com.servegame.yeyyyyyy.luckywheel.LuckyWheel
import com.servegame.yeyyyyyy.luckywheel.extensions.getColoredString

sealed class MenuTitle(var title: String) {
    companion object {
        private val messagesConfig = LuckyWheel.plugin.messagesFileManager.getConfig()
        const val titlePrefix = "menu_title_prefix"
    }

    init {
        this.title = messagesConfig.getColoredString(titlePrefix) + messagesConfig.getColoredString(title)
    }

    object MainMenu : MenuTitle("main_menu_title")
    object LootTableMenu : MenuTitle("loot_table_menu_title")
    object WheelMenu : MenuTitle("wheel_menu_title")
}
