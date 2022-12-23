package com.servegame.yeyyyyyy.luckywheel.utils

import com.servegame.yeyyyyyy.luckywheel.utils.versions.V1_16
import com.servegame.yeyyyyyy.luckywheel.utils.versions.V1_17
import com.servegame.yeyyyyyy.luckywheel.utils.versions.V1_18
import com.servegame.yeyyyyyy.luckywheel.utils.versions.V1_19
import org.bukkit.Bukkit

class VersionChecker {
    companion object {
        val serverVersion = Bukkit.getServer().version
        val VERSIONS = listOf(V1_16.VERSION, V1_17.VERSION, V1_18.VERSION, V1_19.VERSION)

        fun checkVersion(version: String): Boolean {
            return Bukkit.getServer().version.contains(version)
        }

        /**
         * Checks if the used version is equal or posterior to the given one
         * Goes through every version from the given one to the last
         * @return true if the used version is equal or posterior
         */
        fun minVersion(version: String): Boolean {
            val versionIndex = VERSIONS.indexOf(version)
            for (i in versionIndex.until(VERSIONS.size)) {
                if (Bukkit.getServer().version.contains(VERSIONS[i])) return true
            }
            return false
        }
    }
}