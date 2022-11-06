package com.servegame.yeyyyyyy.luckywheel.files

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.servegame.yeyyyyyy.luckywheel.LuckyWheel
import com.servegame.yeyyyyyy.luckywheel.core.models.LootTable
import com.servegame.yeyyyyyy.luckywheel.core.models.serializable.LootTableMapSerializable
import com.servegame.yeyyyyyy.luckywheel.utils.JsonReader
import java.io.File
import java.nio.file.Files

class LootTablesFileManager(val plugin: LuckyWheel) {
    private val fileName = "loot-tables.json"
    private var data: Map<String, LootTable>? = null
    private var jsonFile: File? = null

    init {
        saveData()
    }

    /**
     * Get a [LootTable] that matches the name given.
     * @return The [LootTable] that matches the name, or null if no one matches it.
     */
    fun getLootTable(name: String): LootTable? {
        return getData()[name]
    }

    /**
     * Returns a [List<LootTable>] containing all the LootTables.
     */
    fun getAllLootTables(): List<LootTable> {
        return getData().map { it.value }
    }


    fun addLootTable(lootTable: LootTable) {
        val mutableData = getData().toMutableMap()
        mutableData[lootTable.name] = lootTable
        setData(mutableData.toMap())
        saveData()
    }

    /**
     * Updates a [LootTable] from the collection and saves
     * @return `true` if the [LootTable] existed and was replaced, or `false` if it didn't exist and thus was not
     * replaced.
     */
    fun updateLootTable(lootTable: LootTable): Boolean {
        val map = getData().toMutableMap()
        val wasRemoved = map.replace(lootTable.name, lootTable).isNullOrEmpty()
        setData(map.toMap())
        saveData()
        return wasRemoved
    }

    /**
     * Removes a [LootTable] from data and save.
     */
    fun removeLootTable(lootTable: LootTable) {
        removeLootTable(lootTable.name)
        saveData()
    }

    /**
     * Remove a [LootTable] from data given its name and save.
     */
    fun removeLootTable(name: String) {
        val mutableData = data!!.toMutableMap()
        mutableData.remove(name)
        data = mutableData.toMap()
        saveData()
    }

    fun saveData() {
        val lootTableSerializable = LootTableMapSerializable.fromLootTableMap(getData())
        val gson = GsonBuilder().setPrettyPrinting().create()
        val json = gson.toJson(lootTableSerializable)
        Files.write(jsonFile!!.toPath(), json.toByteArray())
    }

    fun getData(): Map<String, LootTable> {
        if (data == null) {
            reloadConfig()
        }
        return data!!
    }

    private fun setData(data: Map<String, LootTable>) {
        if (this.data == null) {
            reloadConfig()
        }
        this.data = data
    }

    fun reloadConfig() {
        if (jsonFile == null) {
            jsonFile = File(plugin.dataFolder, fileName)
        }
        val defaultStream = plugin.getResource(fileName)
        if (defaultStream != null && !jsonFile!!.exists()) {
            Files.copy(defaultStream, jsonFile!!.toPath())
        }
        val json = JsonReader().readJson(jsonFile!!)
        data = Gson().fromJson(json, LootTableMapSerializable::class.java).toLootTableMap()
    }


}