package com.servegame.yeyyyyyy.luckywheel.core.models.serializable

import com.servegame.yeyyyyyy.luckywheel.core.models.Loot
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class LootSerializable(
    var type: String,
    var amount: Int = 1,
    var weight: Double = 1.0,
    var enchantments: Map<String, Int> = mapOf()
) {

    companion object {
        /**
         * Returns a [LootSerializable] from the given [Loot]
         */
        fun fromLoot(loot: Loot): LootSerializable {
            return LootSerializable(
                loot.item.type.name,
                loot.item.amount,
                loot.weight,
                loot.item.enchantments.mapKeys { enchantment -> enchantment.key.toString() })
        }
    }

    /**
     * Returns a [Loot] from this instance
     */
    fun toLoot(): Loot {
        val itemStack = ItemStack(Material.values().first { mat -> mat.name == type }, amount)
        val enchants =
            enchantments.mapKeys { enchantment -> Enchantment.values().first { enchantment.key == it.toString() } }
        itemStack.addEnchantments(enchants)

        return Loot(itemStack, weight)
    }
}