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

    object Sub1 : MenuOptions("weight_sub_1")
    object Sub01 : MenuOptions("weight_sub_01")
    object Sub001 : MenuOptions("weight_sub_001")
    object Sub0001 : MenuOptions("weight_sub_0001")
    object Add0001 : MenuOptions("weight_add_0001")
    object Add001 : MenuOptions("weight_add_001")
    object Add01 : MenuOptions("weight_add_01")
    object Add1 : MenuOptions("weight_add_1")
}