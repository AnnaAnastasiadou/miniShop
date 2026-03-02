package com.example.minishop.feature.cart

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun dateFormatter(dateString: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
        parser.timeZone = TimeZone.getTimeZone("UTC")

        val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

        val date = parser.parse(dateString)
        date?.let{ formatter.format(it)} ?: ""
    } catch(e: Exception) {
        ""
    }
}