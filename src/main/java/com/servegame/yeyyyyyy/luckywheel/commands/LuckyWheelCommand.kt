package com.servegame.yeyyyyyy.luckywheel.commands

import com.servegame.yeyyyyyy.luckywheel.LuckyWheel
import com.servegame.yeyyyyyy.luckywheel.core.models.LootTable
import com.servegame.yeyyyyyy.luckywheel.core.models.LootTableType
import com.servegame.yeyyyyyy.luckywheel.extensions.getColoredString
import com.servegame.yeyyyyyy.luckywheel.menusystem.Menu
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class LuckyWheelCommand : CommandExecutor {
    private val logger = LuckyWheel.plugin.logger
    private val messagesConfig = LuckyWheel.plugin.messagesFileManager.getConfig()
    private val lootTableFileManager = LuckyWheel.plugin.lootTablesFileManager
    private val subCommands = listOf("create", "createtoken")

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            logger.info(messagesConfig.getColoredString("player_only_command"))
            return true
        }
        val player: Player = sender
        if (!player.hasPermission("luckywheel.command")) {
            player.sendMessage(messagesConfig.getColoredString("luckywheel_command_no_permission"))
            return true
        }
        if (args.isNotEmpty()) {
            if (!subCommands.contains(args[0]) || (args.size != 2)) {
                player.sendMessage(messagesConfig.getColoredString("invalid_parameter"))
                return false
            }
            return runSubCommand(player, args)
        }
        Menu.openMainMenuGui(player)
        return true
    }

    private fun runSubCommand(player: Player, args: Array<out String>): Boolean {
        when (args[0]) {
            "create" -> {
                val lootTableName = args[1]
                lootTableFileManager.addLootTable(LootTable(lootTableName))
                player.sendMessage(
                    messagesConfig.getColoredString("loot_table_created").replace("{lootTable}", lootTableName)
                )
                return true
            }
            "createtoken" -> {
                val lootTableName = args[1]
                lootTableFileManager.addLootTable(LootTable(name = lootTableName, type = LootTableType.TOKEN))
                player.sendMessage(
                    messagesConfig.getColoredString("loot_table_created").replace("{lootTable}", lootTableName)
                )
                return true
            }
        }
        return true
    }
}