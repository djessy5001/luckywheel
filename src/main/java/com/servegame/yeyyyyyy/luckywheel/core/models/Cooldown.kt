package com.servegame.yeyyyyyy.luckywheel.core.models

enum class Cooldown(var minutes: Int? = null) {
    DAILY(-1),
    FULL_DAY(1440),
    HOURLY(-2),
    FULL_HOUR(60),
    WEEKLY(-7),
    FULL_WEEK(10080)
}
