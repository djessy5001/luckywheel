package com.servegame.yeyyyyyy.luckywheel.utils.versions

import org.bukkit.Particle
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionType

class V1_20_5 {
    companion object {
        val VERSION = "1.20.5"
        fun convertLootSerializablePotionMeta(itemStack: ItemStack, potionType: PotionType?) {
            if (itemStack.itemMeta is PotionMeta && potionType != null) {
                val meta = itemStack.itemMeta as PotionMeta
                // reflection
                val metaBasePotionTypeMethod =
                    meta::class.java.getDeclaredMethod("setPotionType", PotionType::class.java)
                metaBasePotionTypeMethod.invoke(meta, potionType)
                // end
                itemStack.itemMeta = meta
            }
        }

        fun happyVillagerParticle(): Particle {
            return Particle.HEART
        }
    }
}
