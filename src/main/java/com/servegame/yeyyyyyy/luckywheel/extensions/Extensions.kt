package com.servegame.yeyyyyyy.luckywheel.extensions

import org.bukkit.ChatColor
import org.bukkit.configuration.file.FileConfiguration


fun FileConfiguration.getColoredString(string: String): String {
    return ChatColor.translateAlternateColorCodes('&', this.getString(string)!!)
}