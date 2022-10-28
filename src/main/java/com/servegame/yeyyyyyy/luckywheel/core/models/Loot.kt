package com.servegame.yeyyyyyy.luckywheel.core.models

import org.bukkit.inventory.ItemStack

/**
 * Represents a loot and its probability.
 *
 * The higher the probability, the more chances it has to be selected.
 */
data class Loot(var item: ItemStack, var probability: Double = 1.0)