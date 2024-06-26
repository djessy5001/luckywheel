package com.servegame.yeyyyyyy.luckywheel.utils

import com.servegame.yeyyyyyy.luckywheel.LuckyWheel
import com.servegame.yeyyyyyy.luckywheel.utils.versions.*
import org.bukkit.Bukkit

class VersionChecker {
    companion object {
        private val logger = LuckyWheel.plugin.logger
        val serverVersion = Bukkit.getServer().version
        val VERSIONS =
            listOf(
                V1_16.VERSION,
                V1_17.VERSION,
                V1_18.VERSION,
                V1_19.VERSION,
                V1_20.VERSION,
                V1_20_5.VERSION,
                V1_21.VERSION
            )

        fun checkVersion(version: String): Boolean {
            return Bukkit.getServer().version.contains(version)
        }

        /**
         * Checks if the used version is equal or posterior to the given one
         * Goes through every version from the given one to the last
         * @return true if the used version is equal or posterior
         */
        fun minVersion(version: String): Boolean {
            val serverVersion = Bukkit.getServer().version
            if (version == V1_20_5.VERSION) {
                if (!serverVersion.contains(Regex("MC: 1.20.[1-4]"))) return true
            } else {
                val versionIndex = VERSIONS.indexOf(version)
                for (i in versionIndex.until(VERSIONS.size)) {
                    if (serverVersion.contains(VERSIONS[i])) return true
                }
            }

            return false
        }
    }
}