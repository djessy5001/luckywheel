package com.servegame.yeyyyyyy.luckywheel.files

import com.servegame.yeyyyyyy.luckywheel.LuckyWheel
import org.bukkit.ChatColor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException
import java.io.InputStreamReader


class MessagesFileManager (private val plugin: LuckyWheel){
    private val logger = LuckyWheel.plugin.logger
    private var dataConfig: FileConfiguration? = null
    private var configFile: File? = null

    init {
        saveDefaultMessagesConfig()
    }

    private fun saveDefaultMessagesConfig() {
        if (this.dataConfig == null || this.configFile == null) {
            return
        }
        try {
            this.getMessagesConfig().save(this.configFile!!)
        } catch (e: IOException) {
            logger.severe(ChatColor.translateAlternateColorCodes('&', "&bLuckyWheel - &4Could not save messages.yml"))
            logger.severe(ChatColor.translateAlternateColorCodes('&', "&bLuckyWheel - &4Check the below message for the reasons!"))
            e.printStackTrace()
        }
    }

    fun getMessagesConfig(): FileConfiguration {
        if (dataConfig == null) {
            this.reloadMessagesConfig()
        }
        return dataConfig!!
    }

    private fun reloadMessagesConfig() {
        if (configFile == null) {
            configFile = File(plugin.dataFolder, "messages.yml")
        }
        dataConfig = YamlConfiguration.loadConfiguration(configFile!!)
        val defaultStream = plugin.getResource("messages.yml")
        if (defaultStream != null) {
            val defaultConfig: YamlConfiguration = YamlConfiguration.loadConfiguration(InputStreamReader(defaultStream))
            (dataConfig as YamlConfiguration).setDefaults(defaultConfig)
        }
    }
}