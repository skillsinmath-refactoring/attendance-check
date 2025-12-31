package com.refactoring_android.math_skill.data.request

import com.google.gson.annotations.SerializedName

data class CheckInRequest(
    @SerializedName("number")
    val number: String,
    @SerializedName("is_sms_disabled")
    val isSmsDisabled: String,
    @SerializedName("class_name_id")
    val classNameId: Int?,
)
