package com.refactoring_android.math_skill.data.request

import com.google.gson.annotations.SerializedName

data class SocialLoginRequest(
    @SerializedName("social_id")
    val socialId: String,
)