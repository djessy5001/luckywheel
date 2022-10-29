package com.servegame.yeyyyyyy.luckywheel.core.models

import org.bukkit.inventory.ItemStack

/**
 * Represents a loot and its weight.
 *
 * The higher the weight, the more chances it has to be selected.
 */
data class Loot(var item: ItemStack, var weight: Double = 1.0)