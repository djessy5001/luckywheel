package com.servegame.yeyyyyyy.luckywheel.menusystem

import org.bukkit.Material

data class MenuItem(var material: Material, var text: String, var pos: Int) {
    companion object {
        val spinTheWheel = MenuItem(Material.RECOVERY_COMPASS, MenuOptions.SpinTheWheel.option, 0)
        val showLootTable = MenuItem(Material.ENCHANTED_BOOK, MenuOptions.ShowLootTable.option, 1)
        val goBack = MenuItem(Material.ARROW, MenuOptions.GoBack.option, 1)
        val exitMenu = MenuItem(Material.IRON_DOOR, MenuOptions.ExitMenu.option, 1)
    }
}