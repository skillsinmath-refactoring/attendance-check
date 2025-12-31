package com.refactoring_android.math_skill.data.response
import com.google.gson.annotations.SerializedName

data class CheckInResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: CheckInData?
)

data class CheckInData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("api_token")
    val apiToken: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("is_at_sugi")
    val isAtSugi: Int,
    @SerializedName("role_id") val roleId: Int?,
    @SerializedName("my_all_class_names")
    val classes: List<ClassInfo> = emptyList()
)

data class ClassInfo(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String,
    @SerializedName("day")
    val day: Int?,
    @SerializedName("start_time")
    val startTime: String?,
    @SerializedName("end_time")
    val endTime: String?,
    @SerializedName("note")
    val note: String?
)



data class CheckInListResponse(
    @SerializedName("code") val code: Int?,
    @SerializedName("data") val userData: CheckInUser?,

)

data class CheckInUser(
    @SerializedName("출석_데이터") val data: List<Attendance>?,
    @SerializedName("상위3명") val top3: List<Attendance>?,
    @SerializedName("출석_학생수") val attendanceCount: Int?,
    @SerializedName("공부중_학생수") val studyingCount: Int?,
)



data class Attendance(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val userId: Int?,
    @SerializedName("class_name") val className: ClassName?,
    @SerializedName("name") val name: String?,
    @SerializedName("check_in") val checkIn: String?,
    @SerializedName("check_out") val checkOut: String?,
    @SerializedName("seconds") val seconds: Int?,
    @SerializedName("user") val user: User?
)

data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("check_in_id") val checkInId: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("transactions") val transactions: List<Transaction>?,
    @SerializedName("att") val att: Int?,
)

data class Transaction(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("balance") val amount: Int,
)

data class ClassName(
    @SerializedName("name") val className: String?,
)

