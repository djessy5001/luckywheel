package com.servegame.yeyyyyyy.luckywheel

import com.servegame.yeyyyyyy.luckywheel.commands.LuckyWheelCommand
import com.servegame.yeyyyyyy.luckywheel.commands.TestCommand
import com.servegame.yeyyyyyy.luckywheel.files.ConfigFileManager
import com.servegame.yeyyyyyy.luckywheel.files.LootTablesFileManager
import com.servegame.yeyyyyyy.luckywheel.files.MessagesFileManager
import com.servegame.yeyyyyyy.luckywheel.menusystem.MenuListener
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
        if (!(Bukkit.getServer().version.contains("1.19"))) {
            consoleSender.sendMessage(prefix + ChatColor.RED.toString() + "-----------------------------------------------")
            consoleSender.sendMessage(prefix + ChatColor.RED.toString() + "LuckyWheel - This plugin only supports 1.19")
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
        messagesFileManager = MessagesFileManager(this)
        configFileManager = ConfigFileManager(this)
        lootTablesFileManager = LootTablesFileManager(this)
    }

    private fun enableCommands() {
        server.getPluginCommand("luckywheel")!!.setExecutor(LuckyWheelCommand())
        server.getPluginCommand("test")!!.setExecutor(TestCommand())
    }

    private fun enableListeners() {
        Bukkit.getPluginManager().registerEvents(MenuListener(), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}