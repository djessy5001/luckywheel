package com.servegame.yeyyyyyy.luckywheel.files

import com.servegame.yeyyyyyy.luckywheel.LuckyWheel
import org.bukkit.configuration.file.YamlConfiguration


class MessagesFileManager(plugin: LuckyWheel, language: String) :
    FileManager(plugin, "messages_$language.yml") {
    override fun getConfig(): YamlConfiguration {
        try {
            return super.getConfig()
        } catch (e: Exception) {
            logger.severe(
                "The language provided in the config.yml file is invalid or was not found, " +
                        "try using 'en', or another supported language, or create your own language file."
            )
            throw Exception(e)
        }
    }
}