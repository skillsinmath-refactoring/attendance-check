package com.refactoring_android.math_skill.data.network.api

import com.refactoring_android.math_skill.data.request.CheckInRequest
import com.refactoring_android.math_skill.data.request.LoginRequest
import com.refactoring_android.math_skill.data.request.SocialLoginRequest
import com.refactoring_android.math_skill.data.response.AndroidTestResponse
import com.refactoring_android.math_skill.data.response.CheckInListResponse
import com.refactoring_android.math_skill.data.response.CheckInResponse
import com.refactoring_android.math_skill.data.response.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface MathSkillApi {
    @GET("api/android/test")
    fun getMessage(): Call<AndroidTestResponse>

    @POST("api/login")
    suspend fun getLoginInfo(@Body request: LoginRequest): LoginResponse

    @POST("api/social/login")
    suspend fun postSocialLogin(@Body request: SocialLoginRequest): LoginResponse

    @GET("api/user/check-in")
    suspend fun getCheckIn(//출석 번호 유효한지 확인
        @Query("check_in_id") checkInId: String,
        @Query("api_token") apiToken: String?
    ): Response<CheckInResponse>

    @GET("/api/checkin/list")
    suspend fun getCheckInList( // 출석 체크 리스트
        @Query("api_token") apiToken: String,
        @Query("date") date: String
    ): CheckInListResponse

    @POST("api/checkin/store")
    suspend fun postCheckIn(//출석체크
        @Header("Authorization") token: String, // Bearer 토큰
        @Body request: CheckInRequest
    ): Response<ResponseBody>

}


