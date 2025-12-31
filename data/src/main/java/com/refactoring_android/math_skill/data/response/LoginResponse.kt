package com.refactoring_android.math_skill.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("user")
    val user: UserInfoResponse
)

data class UserInfoResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("api_token")
    val apiToken: String,
    @SerializedName("is_at_sugi")
    val att: Int,
    @SerializedName("branch")
    val branch: BranchInfo?,
)

data class BranchInfo(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
)
