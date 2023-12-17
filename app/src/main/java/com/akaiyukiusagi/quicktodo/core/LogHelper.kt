package com.akaiyukiusagi.quicktodo.core

import android.util.Log

class LogHelper {

    companion object {
        private const val LOG_KEYWORD = "Log" // 検索用

        private fun getTagAndMessage(message: String): Pair<String, String> {
            val stackTrace = Throwable().stackTrace
            val element = stackTrace[2]

            val tag = "${element.fileName}:${element.lineNumber}"
            val formattedMessage = "$LOG_KEYWORD: ${element.methodName}(): $message"

            return Pair(tag, formattedMessage)
        }

        fun d(message: String) {
            val (tag, formattedMessage) = getTagAndMessage(message)
            Log.d(tag, formattedMessage)
        }

        fun e(error: Exception) {
            val (tag, formattedMessage) = getTagAndMessage(error.toString())
            Log.e(tag, formattedMessage)
        }

    }
}
