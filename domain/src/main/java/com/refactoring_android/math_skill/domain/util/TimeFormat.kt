package com.refactoring_android.math_skill.domain.util

fun Long.toTimeFormat(): String {
    val seconds = this

    val hours = (seconds / 3600).toString().padStart(2, '0')
    val minutes = ((seconds % 3600) / 60).toString().padStart(2, '0')
    val sec = (seconds % 60).toString().padStart(2, '0')

    return "$hours:$minutes:$sec"
}

fun String.toSeconds(): Long {
    val parts = this.split(":").map { it.toIntOrNull() ?: 0 }

    return when (parts.size) {
        3 -> parts[0] * 3600L + parts[1] * 60L + parts[2] // HH:mm:ss
        2 -> parts[0] * 60L + parts[1] // mm:ss (예외 처리)
        1 -> parts[0].toLong() // ss (예외 처리)
        else -> 0L
    }
}
