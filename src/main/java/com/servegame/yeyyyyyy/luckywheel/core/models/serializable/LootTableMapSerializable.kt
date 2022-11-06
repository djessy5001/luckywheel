package com.servegame.yeyyyyyy.luckywheel.core.models.serializable

import com.servegame.yeyyyyyy.luckywheel.core.models.LootTable

data class LootTableMapSerializable(val lootTables: Map<String, LootTableSerializable>) {

    companion object {
        fun fromLootTableMap(lootTableMap: Map<String, LootTable>): LootTableMapSerializable {
            return LootTableMapSerializable(lootTableMap.mapValues { LootTableSerializable.fromLootTable(it.value) })
        }
    }

    fun toLootTableMap(): Map<String, LootTable> {
        return lootTables.mapValues { it.value.toLootTable() }
    }
}