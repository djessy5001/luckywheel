package com.servegame.yeyyyyyy.luckywheel.core.models

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import kotlin.math.ceil
import kotlin.random.Random

class LootTable (
    var name: String = "default",
    private var loots: MutableList<Loot> = mutableListOf(
        Loot(ItemStack(Material.DIAMOND_SWORD), 0.2),
        Loot(ItemStack(Material.DIRT, 64)),
        Loot(ItemStack(Material.GLASS_BOTTLE, 64), 0.8),
        Loot(ItemStack(Material.DIAMOND_BLOCK, 2), 0.1),
        Loot(ItemStack(Material.STONE_SHOVEL, 1)),
        Loot(ItemStack(Material.NETHERITE_BLOCK, 1), 0.05),
        Loot(ItemStack(Material.PACKED_ICE, 64)),
        Loot(ItemStack(Material.DIAMOND_SWORD), 0.2),
        Loot(ItemStack(Material.DIRT, 64)),
        Loot(ItemStack(Material.GLASS_BOTTLE, 64), 0.8),
        Loot(ItemStack(Material.DIAMOND_BLOCK, 2), 0.1),
        Loot(ItemStack(Material.STONE_SHOVEL, 1)),
        Loot(ItemStack(Material.NETHERITE_BLOCK, 1), 0.05),
        Loot(ItemStack(Material.PACKED_ICE, 64))
    )
) : MutableCollection<Loot> {
    override val size: Int = loots.size
    private val maxLootTableSize = 52
    private val totalWeight
        get() = loots.fold(0.0) { acc, loot -> acc + loot.weight }
    val inventorySize
        get() = ceil((loots.size + 2) / 9.0).toInt() * 9

    init {
        loots = loots.subList(0, minOf(maxLootTableSize - 1, loots.size))
    }

    fun getRandomLoot(): Pair<Loot, String> {
        val randomNumber = Random.nextDouble() * totalWeight
        var summedWeight = 0.0

        // goes through the loots list until randomNumber is lower than the summedWeight, then return the current loot
        for (loot in loots) {
            summedWeight += loot.weight
            if (randomNumber < summedWeight) {
                return Pair(loot, getProbabilityOfLootFormatted(loot))
            }
        }
        throw Exception("End of loots reached but no Loot was returned!")
    }

    /**
     * Provide the index of a Loot to get its probability to be chosen
     * @return the probability for the loot to be chosen from 0 (0%) to 1 (100%), or -1 if the index is out of bound
     */
    fun getProbabilityOfLootAt(index: Int): Double {
        if (index in 0.until(loots.size)) {
            return loots[index].weight / totalWeight
        }
        return -1.0
    }

    /**
     * Provide a loot to get its probability to be chosen
     * @return the probability for the loot to be chosen from 0 (0%) to 1 (100%), or -1 if the loot is not in the LootTable
     */
    fun getProbabilityOfLoot(loot: Loot): Double {
        val index = loots.indexOf(loot)
        return getProbabilityOfLootAt(index)
    }

    override fun contains(element: Loot): Boolean {
        return loots.contains(element)
    }

    override fun containsAll(elements: Collection<Loot>): Boolean {
        return loots.containsAll(elements)
    }

    /**
     * Adds the specified element to the end of this list.
     *
     * @return `true` if the item was added, or `false` if `maxLootTableSize` has been reached
     */
    override fun add(element: Loot): Boolean {
        if (loots.size >= maxLootTableSize) return false
        loots.add(element)
        return true
    }

    override fun addAll(elements: Collection<Loot>): Boolean {
        for(loot in elements) {
            if (loots.size >= maxLootTableSize) return false
            loots.add(loot)
        }
        return true
    }

    override fun clear() {
        loots.clear()
    }

    override fun isEmpty(): Boolean {
        return loots.isEmpty()
    }

    override fun iterator(): MutableIterator<Loot> {
        return loots.map { loot -> loot.copy() }.toMutableList().iterator()
    }

    override fun retainAll(elements: Collection<Loot>): Boolean {
        return loots.retainAll(elements)
    }

    override fun removeAll(elements: Collection<Loot>): Boolean {
        return loots.removeAll(elements)
    }

    override fun remove(element: Loot): Boolean {
        return loots.remove(element)
    }

    fun remove(item: ItemStack): Boolean {
        return loots.remove(loots.find { it.item.isSimilar(item) })
    }

    fun getProbabilityOfLootFormatted(loot: Loot): String {
        return "%.2f".format(getProbabilityOfLoot(loot).times(100)) + "%"
    }

    fun getAllItems(): Collection<ItemStack> {
        return loots.map { (item) -> item.clone() }
    }
}