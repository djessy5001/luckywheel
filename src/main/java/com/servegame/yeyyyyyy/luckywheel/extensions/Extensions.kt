package com.servegame.yeyyyyyy.luckywheel.extensions

import com.servegame.yeyyyyyy.luckywheel.LuckyWheel
import com.servegame.yeyyyyyy.luckywheel.utils.ParticleEffect
import org.bukkit.ChatColor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.ItemMeta


fun FileConfiguration.getColoredString(string: String): String {
    return try {
        ChatColor.translateAlternateColorCodes('&', this.getString(string)!!)
    } catch (ex: NullPointerException) {
        LuckyWheel.plugin.logger.warning("String not found: $string")
        ex.printStackTrace()
        string
    }
}

fun Player.spawnParticles(particleEffect: ParticleEffect) {
    val (particle, location, xOffset, yOffset, zOffset, speed, count) = particleEffect
    this.spawnParticle(
        particle, location, count,
        xOffset, yOffset, zOffset, speed
    )
}

fun Array<ItemStack>.listItems(): String {
    return this.joinToString { item -> "${item.amount}x${item.itemMeta?.localizedName}" }
}

fun ItemStack.toText(): String {
    return this.amount.toString() + "x" + this.type.name
}

/**
 * Sets the display name of an ItemStack to the given name
 */
fun ItemStack.withName(name: String): ItemStack {
    return this.apply {
        itemMeta?.apply {
            setDisplayName(name)
        }
    }
}

/**
 * Returns true if the given item matches this instance's [ItemStack.type], [ItemStack.amount] and [ItemStack.getEnchantments]
 */
fun ItemStack.matches(item: ItemStack): Boolean {
    if (itemMeta?.isEnchantedBook() == true && item.itemMeta?.isEnchantedBook() == true) {
        val thisEnchantments = (itemMeta as EnchantmentStorageMeta)
        val itemEnchantments = (item.itemMeta as EnchantmentStorageMeta)
        if (thisEnchantments.hasStoredEnchants() && itemEnchantments.hasStoredEnchants()) {
            val thisEnchantsMap = thisEnchantments.mapEnchantments()
            val itemEnchantsMap = itemEnchantments.mapEnchantments()
            return type == item.type && amount == item.amount && thisEnchantsMap == itemEnchantsMap
        }
    }
    return type == item.type && amount == item.amount
}

fun ItemMeta.isEnchantedBook(): Boolean {
    return this is EnchantmentStorageMeta
}

/**
 * Goes through all the enchantments of an item and returns a map
 * @return a map containing the enchantments names and levels like so:
 * {
 *  "minecraft:looting": 3,
 *  "minecraft:sharpness": 5,
 *  "minecraft:unbreaking": 1
 * }
 */
fun EnchantmentStorageMeta.mapEnchantments(): Map<String, Int> {
    return this.storedEnchants.mapKeys { bookEnchantment -> bookEnchantment.key.key.toString() }
}

fun Player.currentLootTable(): String = this.getMetadata("luckywheel_loot_table")[0].value().toString()

