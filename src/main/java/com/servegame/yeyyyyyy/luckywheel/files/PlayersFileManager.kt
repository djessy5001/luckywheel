package com.servegame.yeyyyyyy.luckywheel.files

import com.servegame.yeyyyyyy.luckywheel.LuckyWheel
import org.bukkit.configuration.MemorySection
import java.time.LocalDateTime
import java.util.*


class PlayersFileManager(plugin: LuckyWheel) : FileManager(plugin, "players.yml") {
    fun logSpin(playerId: UUID, lootTable: String) {
        var playerDataConfig = getConfig().getConfigurationSection(playerId.toString())
        if (playerDataConfig == null) {
            getConfig().createSection(playerId.toString())
            playerDataConfig = getConfig().getConfigurationSection(playerId.toString())!!
        }
        playerDataConfig.set(lootTable, getTimeNow())
        saveConfig()
    }

    fun getLastSpin(playerId: UUID, lootTable: String): LocalDateTime? {
        val data = getConfig().get(playerId.toString()) as MemorySection?
        return LocalDateTime.parse(data?.get(lootTable)?.toString() ?: LocalDateTime.MIN.toString())
    }

    private fun getTimeNow(): String {
        return LocalDateTime.now().toString()
    }
}