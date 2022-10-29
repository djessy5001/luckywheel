package com.servegame.yeyyyyyy.luckywheel.menusystem

import com.servegame.yeyyyyyy.luckywheel.LuckyWheel
import org.bukkit.ChatColor

sealed class MenuTitle(var title: String) {
    companion object {
        private val messagesConfig = LuckyWheel.plugin.messagesFileManager.getConfig()
        val titlePrefix = "&2&l&nLucky Wheel&r - "
    }

    init {
        this.title = ChatColor.translateAlternateColorCodes('&', titlePrefix + messagesConfig.getString(title))
    }

    object MainMenu : MenuTitle("main_menu_title")
    object LootTableMenu : MenuTitle("loottable_menu_title")
    object WheelMenu : MenuTitle("wheel_menu_title")
}
