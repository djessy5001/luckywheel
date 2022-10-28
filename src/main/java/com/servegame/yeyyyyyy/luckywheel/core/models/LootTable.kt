package com.servegame.yeyyyyyy.luckywheel.core.models

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class LootTable {
    var loots: MutableList<Loot> = mutableListOf(Loot(ItemStack(Material.DIAMOND_SWORD)), Loot(ItemStack(Material.DIRT)))
}