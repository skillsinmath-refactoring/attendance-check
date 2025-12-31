package com.refactoring_android.math_skill.data.network

import androidx.core.view.WindowInsetsCompat
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import com.refactoring_android.math_skill.data.constant.URLCONSTANT.BASE_URL_PRODUCTION
import com.refactoring_android.math_skill.data.constant.URLCONSTANT.BASE_URL_STAGE
import com.refactoring_android.math_skill.data.response.Attendance
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.net.Proxy


object NetworkService {
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(logging) // 원래 okhttp 로그
        .build()


    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(
            BASE_URL_PRODUCTION
        ) // 에뮬레이터에서 PC의 localhost 접근
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

}
