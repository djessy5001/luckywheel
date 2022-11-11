package com.servegame.yeyyyyyy.luckywheel.extensions

import com.servegame.yeyyyyyy.luckywheel.LuckyWheel
import com.servegame.yeyyyyyy.luckywheel.utils.ParticleEffect
import org.bukkit.ChatColor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


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
 * Returns true if the given item matches this instance's [ItemStack.type], [ItemStack.amount] and [ItemStack.getEnchantments]
 */
fun ItemStack.matches(item: ItemStack): Boolean {
    return this.type == item.type && this.amount == item.amount && this.enchantments == item.enchantments
}

fun Player.currentLootTable(): String = this.getMetadata("luckywheel_loot_table")[0].value().toString()

