package com.servegame.yeyyyyyy.luckywheel.core.models.serializable

import com.servegame.yeyyyyyy.luckywheel.core.models.Loot
import com.servegame.yeyyyyyy.luckywheel.extensions.mapEnchantments
import com.servegame.yeyyyyyy.luckywheel.utils.VersionChecker
import com.servegame.yeyyyyyy.luckywheel.utils.versions.V1_20_5
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionType

class LootSerializable(
    var type: String,
    var amount: Int = 1,
    var weight: Double = 1.0
) {
    var enchantments: Map<String, Int>? = null
    var bookEnchantments: Map<String, Int>? = null
    var potionData: PotionData? = null
    var potionType: PotionType? = null

    companion object {
        /**
         * Returns a [LootSerializable] from the given [Loot]
         */
        fun fromLoot(loot: Loot): LootSerializable {
            val lootSerializable = LootSerializable(
                loot.item.type.name,
                loot.item.amount,
                loot.weight
            )
            if (loot.item.enchantments.isNotEmpty()) {
                lootSerializable.enchantments =
                    loot.item.enchantments.mapKeys { enchantment -> enchantment.key.key.toString() }
            }
            convertLootBookMeta(loot, lootSerializable)
            convertLootPotionMeta(loot, lootSerializable)


            return lootSerializable
        }

        private fun convertLootBookMeta(
            loot: Loot,
            lootSerializable: LootSerializable
        ) {
            val itemMeta = loot.item.itemMeta
            if (itemMeta is EnchantmentStorageMeta) {
                val bookEnchantments = (loot.item.itemMeta as EnchantmentStorageMeta)
                if (bookEnchantments.hasStoredEnchants()) {
                    lootSerializable.bookEnchantments =
                        bookEnchantments.mapEnchantments()
                }
            }
        }

        private fun convertLootPotionMeta(
            loot: Loot,
            lootSerializable: LootSerializable
        ) {
            val itemMeta = loot.item.itemMeta
            if (itemMeta is PotionMeta) {
                val potionMeta = (loot.item.itemMeta as PotionMeta)
                lootSerializable.potionData = potionMeta.basePotionData
            }
        }
    }

    /**
     * Returns a [Loot] from this instance
     */
    fun toLoot(): Loot {
        val itemStack = ItemStack(Material.values().first { mat -> mat.name == type }, amount)
        convertLootSerializablePotionMeta(itemStack)
        convertLootSerializableBookMeta(itemStack)
        if (!enchantments.isNullOrEmpty()) {
            val enchants =
                enchantments!!.mapKeys { enchantment ->
                    Enchantment.values().first { enchantment.key == it.key.toString() }
                }
            itemStack.addUnsafeEnchantments(enchants)
        }
        return Loot(itemStack, weight)
    }

    private fun convertLootSerializablePotionMeta(
        itemStack: ItemStack,
    ) {
        if (VersionChecker.minVersion(V1_20_5.VERSION)) {
            V1_20_5.convertLootSerializablePotionMeta(itemStack, potionType)
        } else {
            if (itemStack.itemMeta is PotionMeta && potionData != null) {
                val meta = itemStack.itemMeta as PotionMeta
                meta.basePotionData = potionData!!
                itemStack.itemMeta = meta
            }
        }

    }

    private fun convertLootSerializableBookMeta(itemStack: ItemStack) {
        if (itemStack.itemMeta is EnchantmentStorageMeta && !bookEnchantments.isNullOrEmpty()) {
            bookEnchantments!!.forEach { bookEnchant ->
                val meta = itemStack.itemMeta as EnchantmentStorageMeta
                val enchant = Enchantment.values().first { bookEnchant.key == it.key.toString() }
                meta.addStoredEnchant(enchant, bookEnchant.value, true)
                itemStack.itemMeta = meta
            }
        }
    }
}