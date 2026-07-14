package com.bosandroidapp.aopaykit.utils

import android.os.Environment
import android.util.Log
import java.io.File

object Logger {
    fun d(tag: String, logMessage: String) {
        Log.d(tag, logMessage)
        printInFile("DEBUG",tag,logMessage)
    }

    fun v(tag: String, logMessage: String) {
        Log.v(tag, logMessage)
        printInFile("VERBOSE",tag,logMessage)
    }

    fun i(tag: String, logMessage: String) {
        Log.i(tag, logMessage)
        printInFile("INFO",tag,logMessage)
    }

    fun e(tag: String, logMessage: String) {
        Log.e(tag, logMessage)
        printInFile("ERROR",tag,logMessage)
    }

    private fun printInFile(type: String, tag: String, logMessage: String) {
        try {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs()
            }
            val file = File(downloadsDir, "EmiClubLogs.txt")
            if (!file.exists()) {
                file.createNewFile()
            }
            val existingText = file.readText()
            val finalText = if (existingText.isNotEmpty()) {
                "$existingText\n$type: $tag => $logMessage"
            } else {
                "$type: $tag => $logMessage"
            }
            file.writeText(finalText)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}