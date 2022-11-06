package com.servegame.yeyyyyyy.luckywheel.commands

import com.servegame.yeyyyyyy.luckywheel.LuckyWheel
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class TestCommand : CommandExecutor {
    private val logger = LuckyWheel.plugin.logger
    private val messagesConfig = LuckyWheel.plugin.messagesFileManager.getConfig()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        logger.info("total proba")
        val lootTable = LuckyWheel.plugin.lootTablesFileManager.getLootTable("default")!!
        logger.info(lootTable.getRandomLoot().toString())

        return true
    }
}