package com.servegame.yeyyyyyy.luckywheel.core.models.serializable

import com.servegame.yeyyyyyy.luckywheel.core.models.Cooldown
import com.servegame.yeyyyyyy.luckywheel.core.models.LootTable


class LootTableSerializable(var name: String, var cooldown: String, var lootsSerializable: List<LootSerializable>) {
    companion object {
        /**
         * Returns a [LootSerializable] from the given [LootTable]
         */
        fun fromLootTable(lootTable: LootTable): LootTableSerializable {
            val lootsSerializable = lootTable.map { loot -> LootSerializable.fromLoot(loot) }
            return LootTableSerializable(lootTable.name, lootTable.cooldown.name, lootsSerializable)
        }
    }

    /**
     * Returns a [LootTable] from this serializable instance
     */
    fun toLootTable(): LootTable {
        val loots = lootsSerializable.map { loot -> loot.toLoot() }
        return LootTable(name, loots.toMutableList(), Cooldown.valueOf(cooldown))
    }
}