package com.servegame.yeyyyyyy.luckywheel.commands

import com.servegame.yeyyyyyy.luckywheel.LuckyWheel
import com.servegame.yeyyyyyy.luckywheel.core.models.LootTable
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
            if (args[0] != "create" || args.size != 2) {
                player.sendMessage(messagesConfig.getColoredString("invalid_parameter"))
                return false
            }
            val lootTableName = args[1]
            lootTableFileManager.addLootTable(LootTable(lootTableName))
            player.sendMessage(
                messagesConfig.getColoredString("loot_table_created").replace("{lootTable}", lootTableName)
            )
            return true
        }
        Menu.openMainMenuGui(player)
        return true
    }
}