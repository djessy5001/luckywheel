package com.servegame.yeyyyyyy.luckywheel.core.models

import com.servegame.yeyyyyyy.luckywheel.exceptions.LootTableNotFoundException
import com.servegame.yeyyyyyy.luckywheel.extensions.matches
import com.servegame.yeyyyyyy.luckywheel.extensions.toText
import com.servegame.yeyyyyyy.luckywheel.utils.minToHoursAndMin
import org.bukkit.inventory.ItemStack
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.pow
import kotlin.random.Random

class LootTable(
    var name: String = "default",
    private var loots: MutableList<Loot> = mutableListOf(),
    var cooldown: Cooldown = Cooldown.DAILY
) : MutableCollection<Loot> {
    override val size: Int get() = loots.size
    private val maxLootSize = 51
    private val totalWeight
        get() = loots.fold(0.0) { acc, loot -> acc + loot.weight }


    init {
        loots = loots.subList(0, minOf(maxLootSize - 1, loots.size))
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

    /**
     * Returns a [Loot] from this [LootTable] matching the given [ItemStack]'s data (excepting metadata)
     */
    fun getLoot(item: ItemStack): Loot {
        return this.find { loot -> loot.item.matches(item) }
            ?: throw LootTableNotFoundException("No matching loot found in LootTable '$name' for item '${item.toText()}'")
    }

    fun getProbabilityOfLootFormatted(loot: Loot): String {
        return "%.2f".format(getProbabilityOfLoot(loot).times(100)) + "%"
    }

    fun getLuck(loot: Loot): Int {
        val probability = getProbabilityOfLoot(loot)
        // Exponential function from 5-1
        val luck = (5 * Math.E.pow(-0.016 * (probability * 100))).toInt()
        return luck
    }

    fun canSpin(lastSpinDate: LocalDateTime?): Pair<Boolean, Pair<String, Boolean>?> {
        if (lastSpinDate == null) return Pair(true, null)
        val now = LocalDateTime.now()
        val dateDiffInMinutes = ChronoUnit.MINUTES.between(lastSpinDate, LocalDateTime.now())
        val canSpin: Boolean
        val needsTranslation: Boolean
        val text: String
        when (cooldown) {
            Cooldown.DAILY -> {
                val isAnotherDay = now.toLocalDate() > lastSpinDate.toLocalDate()
                canSpin = isAnotherDay
                text = "until_tomorrow"
                needsTranslation = true
            }
            Cooldown.WEEKLY -> {
                val isAnotherWeek = now.toLocalDate()
                    .compareTo(lastSpinDate.toLocalDate()) > 7 || now.dayOfWeek < lastSpinDate.dayOfWeek
                canSpin = isAnotherWeek
                text = "until_next_week"
                needsTranslation = true
            }
            Cooldown.HOURLY -> {
                val isAnotherHour = now.toLocalDate() > lastSpinDate.toLocalDate() || now.hour > lastSpinDate.hour
                canSpin = isAnotherHour
                text = "until_next_hour"
                needsTranslation = true
            }
            else -> {
                canSpin = dateDiffInMinutes > cooldown.minutes!!
                text = minToHoursAndMin(cooldown.minutes!! - dateDiffInMinutes)
                needsTranslation = false
            }
        }
        return Pair(canSpin, Pair(text, needsTranslation))
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
        if (loots.size >= maxLootSize) return false
        loots.add(element)
        return true
    }

    override fun addAll(elements: Collection<Loot>): Boolean {
        for(loot in elements) {
            if (loots.size >= maxLootSize) return false
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
        return loots.toMutableList().iterator()
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
        return loots.remove(loots.find { loot -> loot.item.matches(item) })
    }

    fun getAllItems(): Collection<ItemStack> {
        return loots.map { (item) -> item.clone() }
    }

    /**
     * Returns the luck of the player on a 1 to 5 exponential range
     */

}