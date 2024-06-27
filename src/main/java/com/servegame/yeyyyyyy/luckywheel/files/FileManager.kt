package com.servegame.yeyyyyyy.luckywheel.files

import com.servegame.yeyyyyyy.luckywheel.LuckyWheel
import org.bukkit.ChatColor
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

open class FileManager(val plugin: LuckyWheel, var fileName: String = "config.yml") : IFileManager {
    protected val logger = LuckyWheel.plugin.logger
    protected var dataConfig: YamlConfiguration? = null
    protected var configFile: File? = null

    init {
        saveConfig()
    }

    final override fun saveConfig() {
        try {
            this.getConfig().save(this.configFile!!)
        } catch (e: IOException) {
            logger.severe(ChatColor.translateAlternateColorCodes('&', "&bLuckyWheel - &4Could not save $fileName"))
            logger.severe(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    "&bLuckyWheel - &4Check the below message for the reasons!"
                )
            )
            e.printStackTrace()
        }
    }

    override fun getConfig(): YamlConfiguration {
        if (dataConfig == null) {
            this.reloadConfig()
        }
        return dataConfig!!
    }

    override fun reloadConfig() {
        if (configFile == null) {
            configFile = File(plugin.dataFolder, fileName)
        }
        val defaultStream = plugin.getResource(fileName)
        if (defaultStream != null) {
            val defaultConfig: YamlConfiguration =
                YamlConfiguration.loadConfiguration(InputStreamReader(defaultStream, StandardCharsets.UTF_8))
            if (!configFile!!.exists()) {
                defaultConfig.save(configFile!!)
            }
            dataConfig = YamlConfiguration.loadConfiguration(configFile!!)
            dataConfig!!.setDefaults(defaultConfig)
            dataConfig!!.options().copyDefaults(true)
        }
    }
}