package com.servegame.yeyyyyyy.luckywheel.utils.versions

import org.bukkit.entity.Firework

class V1_19 {
    companion object {
        val VERSION = "1.19"

        fun setFireworkMaxLife(firework: Firework, maxLife: Int) {
            val fireworkMaxLifeField = Firework::class.java.getDeclaredField("setMaxLife")
            fireworkMaxLifeField.isAccessible = true
            fireworkMaxLifeField.setInt(firework, maxLife)
        }
    }

}
