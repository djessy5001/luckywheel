package com.servegame.yeyyyyyy.luckywheel.utils

import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths

class JsonReader {
    fun readJson(path: String): String {
        return String(Files.readAllBytes(Paths.get(path)))
    }

    fun readJson(file: File): String {
        return String(file.readBytes())
    }

    fun readJson(inputStream: InputStream): String {
        return String(inputStream.readBytes())
    }

    
}