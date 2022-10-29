package com.servegame.yeyyyyyy.luckywheel

import com.servegame.yeyyyyyy.luckywheel.commands.LuckyWheelCommand
import com.servegame.yeyyyyyy.luckywheel.commands.TestCommand
import com.servegame.yeyyyyyy.luckywheel.files.MessagesFileManager
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin

class LuckyWheel : JavaPlugin() {
    companion object {
        lateinit var plugin: LuckyWheel
    }

    lateinit var messagesFileManager: MessagesFileManager
    private val prefix = ChatColor.translateAlternateColorCodes('&', "&8[&2&lLuckyWheel&8] ")

    override fun onEnable() {
        // Plugin startup logic
        // Establishing static access to this plugin
        plugin = this

        if (!checkVersionCompatible()) return
        loadConfigFile()
        loadFileManagers()
        enableCommands()
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

    private fun loadConfigFile() {
        config.options().copyDefaults()
        saveDefaultConfig()
    }

    private fun loadFileManagers() {
        this.messagesFileManager = MessagesFileManager(this)
    }

    private fun enableCommands() {
        getCommand("luckywheel")!!.setExecutor(LuckyWheelCommand())
        getCommand("test")!!.setExecutor(TestCommand())
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}