package com.refactoring_android.math_skill.domain

data class UserInfo(
    val id: Int,
    val type: String,
    val name: String,
    val apiToken: String,
    val att: Int,
    val roleId: Int,
)
