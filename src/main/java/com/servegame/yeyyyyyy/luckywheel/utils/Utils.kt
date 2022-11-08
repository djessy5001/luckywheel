package com.servegame.yeyyyyyy.luckywheel.utils

import kotlin.math.ceil
import kotlin.math.min

/**
 * Returns the nearest multiple of 9 non-inferior to the provided integer (máx 54)
 * - used for creating inventories
 */
fun getNearestCeilMultipleOfNine(number: Int): Int {
    return min((ceil(number / 9.0).toInt() * 9), 54)
}