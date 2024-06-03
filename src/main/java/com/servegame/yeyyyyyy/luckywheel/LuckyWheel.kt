package com.servegame.yeyyyyyy.luckywheel

import com.servegame.yeyyyyyy.luckywheel.commands.LuckyWheelCommand
import com.servegame.yeyyyyyy.luckywheel.files.ConfigFileManager
import com.servegame.yeyyyyyy.luckywheel.files.LootTablesFileManager
import com.servegame.yeyyyyyy.luckywheel.files.MessagesFileManager
import com.servegame.yeyyyyyy.luckywheel.files.PlayersFileManager
import com.servegame.yeyyyyyy.luckywheel.menusystem.MenuListener
import com.servegame.yeyyyyyy.luckywheel.utils.VersionChecker
import com.servegame.yeyyyyyy.luckywheel.utils.versions.V1_16
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin

class LuckyWheel : JavaPlugin() {
    companion object {
        lateinit var plugin: LuckyWheel
    }

    lateinit var messagesFileManager: MessagesFileManager
    lateinit var configFileManager: ConfigFileManager
    lateinit var lootTablesFileManager: LootTablesFileManager
    lateinit var playersFileManager: PlayersFileManager
    private val prefix = ChatColor.translateAlternateColorCodes('&', "&8[&2&lLuckyWheel&8] ")

    override fun onEnable() {
        // Plugin startup logic
        // Establishing static access to this plugin
        plugin = this

        if (!checkVersionCompatible()) return
        loadFileManagers()
        enableCommands()
        enableListeners()
    }


    private fun checkVersionCompatible(): Boolean {
        val consoleSender = Bukkit.getConsoleSender()
        if (!VersionChecker.minVersion(V1_16.VERSION)) {
            consoleSender.sendMessage(prefix + ChatColor.RED.toString() + "-----------------------------------------------")
            consoleSender.sendMessage(prefix + ChatColor.RED.toString() + "LuckyWheel - This plugin only supports 1.16+")
            consoleSender.sendMessage(prefix + ChatColor.RED.toString() + "Current version is " + VersionChecker.serverVersion)
            consoleSender.sendMessage(prefix + ChatColor.RED.toString() + "LuckyWheel - Is now disabling!")
            consoleSender.sendMessage(prefix + ChatColor.RED.toString() + "-----------------------------------------------")
            Bukkit.getPluginManager().disablePlugin(this)
            return false
        }
        consoleSender.sendMessage(prefix + ChatColor.GREEN.toString() + "-----------------------------------------------------------")
        consoleSender.sendMessage(prefix + ChatColor.GREEN.toString() + "LuckyWheel - This server is running a compatible version")
        consoleSender.sendMessage(prefix + ChatColor.GREEN.toString() + "-----------------------------------------------------------")
        return true
    }


    private fun loadFileManagers() {
        configFileManager = ConfigFileManager(this)
        messagesFileManager = MessagesFileManager(this, config.getString("locale") ?: "en")
        lootTablesFileManager = LootTablesFileManager(this)
        playersFileManager = PlayersFileManager(this)
    }

    private fun enableCommands() {
        server.getPluginCommand("luckywheel")!!.setExecutor(LuckyWheelCommand())
    }

    private fun enableListeners() {
        Bukkit.getPluginManager().registerEvents(MenuListener(), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}