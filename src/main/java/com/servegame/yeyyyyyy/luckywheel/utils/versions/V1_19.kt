package com.servegame.yeyyyyyy.luckywheel.utils.versions

import org.bukkit.entity.Firework

class V1_19 {
    companion object {
        val VERSION = "1.19"

        fun setFireworkMaxLife(firework: Firework, maxLife: Int) {
            val fireworkMaxLifeMethod = firework::class.java.getDeclaredMethod("setMaxLife", Int::class.java)
            fireworkMaxLifeMethod.invoke(firework, maxLife)
        }
    }

}
