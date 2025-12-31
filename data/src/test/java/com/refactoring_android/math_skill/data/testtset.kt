package com.refactoring_android.math_skill.data

import android.util.Log
import com.google.gson.GsonBuilder
import com.refactoring_android.math_skill.data.network.api.MathSkillApi
import com.refactoring_android.math_skill.data.request.CheckInRequest
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



private val apiService: MathSkillApi by lazy {
    Retrofit.Builder()
        .baseUrl("http://127.0.0.1:8000/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(MathSkillApi::class.java)
}
class testtest {
  /*  @Test
    fun testCheckIn() = runBlocking {
        try {
            val token = "Bearer qvvtRm3L8c0XHc46wphd7JgEg1dtNViOdRaEkVIRSns3URw8clGNuEiU2siB"
            val request = CheckInRequest(number = "2541", isSmsDisabled = "true")

            // POST 요청 보내기
            val response = apiService.postCheckIn(token, request)

            // 응답 체크
            if (response.isSuccessful) {
                val jsonString = response.body()?.string()
                val jsonObject = JSONObject(jsonString)

                if (jsonObject.has("data")) {
                    val dataObject = jsonObject.optJSONObject("data")
                    val name = dataObject?.optString("name")
                    println("응답 데이터: $name")
                } else {
                    val code = jsonObject.optString("code")
                    println("응답 코드: $code")
                }
            } else {
                println("Error: ${response.code()}")
            }

        } catch (e: Exception) {
            println("API 호출 실패: ${e.message}")
        }
    }*/
}
