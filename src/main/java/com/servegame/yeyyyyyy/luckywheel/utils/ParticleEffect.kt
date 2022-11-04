package com.servegame.yeyyyyyy.luckywheel.utils

import org.bukkit.Location
import org.bukkit.Particle

data class ParticleEffect(
    var particle: Particle,
    var location: Location,
    var xOffset: Double = 0.0,
    var yOffset: Double = 2.0,
    var zOffset: Double = 0.0,
    var speed: Double = 1.0,
    var count: Int = 10,
)