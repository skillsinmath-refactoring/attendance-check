package com.refactoring_android.math_skill.domain

data class StudentInfo (
    val id: Int,
    val studentId: Int,
    val name: String,
    val checkIn: String?,
    val checkOut: String?,
    val studyTime: String?,
    val rank: Int? = null
){
    val isStudying: Boolean
        get() = checkIn != null && checkOut == null && studyTime != null
}