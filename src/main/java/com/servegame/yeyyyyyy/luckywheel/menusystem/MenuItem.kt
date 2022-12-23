package com.servegame.yeyyyyyy.luckywheel.menusystem

import com.servegame.yeyyyyyy.luckywheel.utils.VersionChecker
import com.servegame.yeyyyyyy.luckywheel.utils.versions.V1_19
import org.bukkit.Material

data class MenuItem(var material: Material, var text: String, var pos: Int) {
    companion object {
        val spinTheWheel = if (VersionChecker.minVersion(V1_19.VERSION))
            MenuItem(
                Material.valueOf("RECOVERY_COMPASS"),
                MenuOptions.SpinTheWheel.option,
                0
            ) else MenuItem(Material.COMPASS, MenuOptions.SpinTheWheel.option, 0)
        val showLootTable = MenuItem(Material.ENCHANTED_BOOK, MenuOptions.ShowLootTable.option, 1)
        val goBack = MenuItem(Material.ARROW, MenuOptions.GoBack.option, 2)
        val exitMenu = MenuItem(Material.IRON_DOOR, MenuOptions.ExitMenu.option, 3)
    }
}