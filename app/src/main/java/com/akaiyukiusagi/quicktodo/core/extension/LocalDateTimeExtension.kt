package com.akaiyukiusagi.quicktodo.core.extension

import android.content.Context
import com.akaiyukiusagi.quicktodo.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * 2023/12/17 のようなフォーマットでLocalDateTimeを文字列に変換する
 */
fun LocalDateTime?.view(): String {
    val formatter = DateTimeFormatter.ofPattern("MM/dd")
    return this?.format(formatter) ?: ""
}

/**
 * 何日前かをまとめるヘッダー
 */
fun LocalDateTime?.category(context: Context): String {
    if (this == null) return ""

    val now = LocalDateTime.now()
    return when (ChronoUnit.DAYS.between(this.toLocalDate(), now.toLocalDate())) {
        0L -> context.getString(R.string.label_today)
        1L -> context.getString(R.string.label_yesterday)
        2L -> context.getString(R.string.label_day_before_yesterday)
        in 3..7 -> context.getString(R.string.label_last_7_days)
        in 8..30 -> context.getString(R.string.label_last_30_days)
        else -> context.getString(R.string.label_month, this.month.value)
    }
}