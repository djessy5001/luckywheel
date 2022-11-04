package com.servegame.yeyyyyyy.luckywheel.menusystem

import com.servegame.yeyyyyyy.luckywheel.LuckyWheel
import com.servegame.yeyyyyyy.luckywheel.extensions.getColoredString

sealed class MenuOptions(var option: String) {
    companion object {
        private val messagesConfig = LuckyWheel.plugin.messagesFileManager.getConfig()
    }

    init {
        option =
            messagesConfig.getColoredString(option)
    }

    object SpinTheWheel : MenuOptions("spin_the_wheel")
    object ShowLootTable : MenuOptions("show_loot_table")
    object GoBack : MenuOptions("go_back_menu")
    object ExitMenu : MenuOptions("exit_menu")
}