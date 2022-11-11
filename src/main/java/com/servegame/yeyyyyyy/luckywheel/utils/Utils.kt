package com.servegame.yeyyyyyy.luckywheel.utils

import org.bukkit.Color
import org.bukkit.FireworkEffect
import kotlin.math.ceil
import kotlin.math.min

/**
 * Returns the nearest multiple of 9 non-inferior to the provided integer (máx 54)
 * - used for creating inventories
 */
fun getNearestCeilMultipleOfNine(number: Int): Int {
    return min((ceil(number / 9.0).toInt() * 9), 54)
}

/**
 * Returns a random firework effect
 */
fun randomFireworkEffect(): FireworkEffect {
    return FireworkEffect.builder()
        .trail(randomBool())
        .withFade(randomColor())
        .withColor(randomColor())
        .flicker(randomBool())
        .with(FireworkEffect.Type.values().random())
        .build()
}

fun randomColor(): Color {
    return Color.fromBGR(0.until(16777216).random())
}

fun randomBool(): Boolean {
    return (0..1).random() == 1
}