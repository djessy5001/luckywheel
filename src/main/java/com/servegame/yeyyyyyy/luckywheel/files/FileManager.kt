package com.servegame.yeyyyyyy.luckywheel.files

import org.bukkit.configuration.file.FileConfiguration

interface FileManager {
    fun saveDefaultConfig()
    fun getConfig(): FileConfiguration
    fun reloadConfig()
}