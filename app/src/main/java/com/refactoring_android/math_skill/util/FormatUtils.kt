package com.refactoring_android.math_skill.util

fun Int.formatSecondsToTime(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val secs = this % 60
    return String.format("%02d:%02d:%02d", hours, minutes, secs)
}
