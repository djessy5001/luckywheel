package com.servegame.yeyyyyyy.luckywheel.files

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.servegame.yeyyyyyy.luckywheel.LuckyWheel
import com.servegame.yeyyyyyy.luckywheel.core.models.LootTable
import com.servegame.yeyyyyyy.luckywheel.core.models.serializable.LootTableMapSerializable
import com.servegame.yeyyyyyy.luckywheel.exceptions.LootTableNotFoundException
import com.servegame.yeyyyyyy.luckywheel.utils.JsonReader
import java.io.File
import java.nio.file.Files

/**
 * This class is used to manage the [LootTable]s and store them in the json file loot-tables.json
 *
 * * This FileManager does not extend the [FileManager] class, since it does not use YAML configuration files
 */
class LootTablesFileManager(private val plugin: LuckyWheel) {
    private val fileName = "loot-tables.json"
    private var data: Map<String, LootTable>? = null
    private var jsonFile: File? = null

    init {
        saveData()
    }

    /**
     * Get a [LootTable] that matches the name given.
     * @return The [LootTable] that matches the name.
     * @throws [LootTableNotFoundException] when no [LootTable] matching [name] is found.
     */
    fun getLootTable(name: String): LootTable {
        return getData()[name] ?: throw LootTableNotFoundException("No LootTable found for name '$name'")
    }

    /**
     * Returns a [List]<[LootTable]> containing all the LootTables.
     */
    fun getAllLootTables(): List<LootTable> {
        return getData().map { it.value }
    }


    /**
     * Adds a LootTable and saves.
     * @return true if the table was inserted or false if the list is full (máx 51 LootTables)
     */
    fun addLootTable(lootTable: LootTable): Boolean {
        val lootTables = getData().toMutableMap()
        if (lootTables.size >= 51) return false
        lootTables[lootTable.name] = lootTable
        setData(lootTables.toMap())
        saveData()
        return true
    }

    /**
     * Updates a [LootTable] from the collection and saves.
     */
    fun updateLootTable(lootTable: LootTable) {
        val map = getData().toMutableMap()
        map.replace(lootTable.name, lootTable).isNullOrEmpty()
        setData(map.toMap())
        saveData()
    }

    /**
     * Removes a [LootTable] from data and saves.
     */
    fun removeLootTable(lootTable: LootTable) {
        removeLootTable(lootTable.name)
    }

    /**
     * Removes a [LootTable] from data given its name and saves.
     */
    fun removeLootTable(name: String) {
        val mutableData = data!!.toMutableMap()
        mutableData.remove(name)
        data = mutableData.toMap()
        saveData()
    }

    /**
     * Saves the LootTable configuration to the json file
     */
    fun saveData() {
        val lootTableSerializable = LootTableMapSerializable.fromLootTableMap(getData())
        val gson = GsonBuilder().setPrettyPrinting().create()
        val json = gson.toJson(lootTableSerializable)
        Files.write(jsonFile!!.toPath(), json.toByteArray())
    }

    /**
     * Returns the data as [Map]<[String], [LootTable]>
     */
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