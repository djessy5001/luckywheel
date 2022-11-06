package com.servegame.yeyyyyyy.luckywheel.files

import org.bukkit.configuration.file.FileConfiguration

interface IFileManager {
    fun saveConfig()

    fun getConfig(): FileConfiguration

    fun reloadConfig()
}