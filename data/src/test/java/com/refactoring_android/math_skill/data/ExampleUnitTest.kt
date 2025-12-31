package com.refactoring_android.math_skill.data

import android.util.Log
import com.google.gson.GsonBuilder
import com.refactoring_android.math_skill.data.constant.URLCONSTANT.BASE_URL_STAGE
import com.refactoring_android.math_skill.data.network.api.MathSkillApi
import com.refactoring_android.math_skill.data.request.CheckInRequest
import com.refactoring_android.math_skill.data.request.LoginRequest
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.Test

import org.junit.Assert.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}

val gson = GsonBuilder()
    .setLenient()  // lenient 모드를 활성화
    .create()


private val apiService: MathSkillApi by lazy {
    Retrofit.Builder()
        .baseUrl("http://127.0.0.1:8000/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(MathSkillApi::class.java)
}

class ApiTest {
  /*  @Test
    @Headers("Accept: application/json")
    fun testCheckInApiCall(): Unit = runBlocking {
        try {
            val response = apiService.getCheckIn( // 존재하는 번호인지 확인
                checkInId = "2174",
                apiToken = "qvvtRm3L8c0XHc46wphd7JgEg1dtNViOdRaEkVIRSns3URw8clGNuEiU2siB"
            )

                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        val jsonString = responseBody.string()
                        println("CheckIn응답"+"JSON: $jsonString") // ← 전체 JSON 확인

                        val jsonObject = JSONObject(jsonString)

                        if (jsonObject.has("data")) {
                            val dataObject = jsonObject.optJSONObject("data")
                            val name = dataObject?.optString("name")
                            Log.d("CheckIn으아아", "Name: $name")
                        } else {
                            val name = jsonObject.optString("name")
                            Log.d("CheckIn", "Name: $name")
                        }

                    }
                } else {
                    println("CheckIn"+"Error: ${response.code()}")
                }
            } catch (e: Exception) {
            println("API_ERROR"+"오류: ${e.message}")
            }



    }*/

    @Test
    fun testLoginApiCall() = runBlocking {
        val request = LoginRequest(
            email = "joe@navycode.co.kr",
            password = "rfrf0404!!"
        )
        try {
            val response = apiService.getLoginInfo(request)
            println("응답: ${response.message} | ${response.user}")
        } catch (e: Exception) {
            println("오류 발생: ${e.message}")
        }
    }


    @Test
    fun testCheckInList(): Unit = runBlocking {
        try {
            val a= apiService.getCheckInList(
                apiToken = "qvvtRm3L8c0XHc46wphd7JgEg1dtNViOdRaEkVIRSns3URw8clGNuEiU2siB",
                //"qvvtRm3L8c0XHc46wphd7JgEg1dtNViOdRaEkVIRSns3URw8clGNuEiU2siB",
                date = "2024-04-11"
            )

            println(a.userData?.data)
          //  println(a.userData?.top3)

        } catch (e: Exception) {
            println("오류 발생: ${e.message}")
        }
    }

  /*  @Test
    fun testCheckIn(): Unit = runBlocking { //출석체크
        try {
            val token = "Bearer qvvtRm3L8c0XHc46wphd7JgEg1dtNViOdRaEkVIRSns3URw8clGNuEiU2siB"
            val request = CheckInRequest(
                number = "2541",
                isSmsDisabled = "true"
            )
            // POST 요청 보내기
            val response = apiService.postCheckIn(token, request)
            println("으아아Cq"+ "Name:")
            if (response.isSuccessful) {
                val jsonString = response.body()?.string() // 첫 번째로 body()를 호출해 응답을 가져옵니다.
                if (jsonString != null) {
                    println("으아아애비좆같은샊;ㅣ: $jsonString")  // JSON 응답 출력
                    try {
                        val jsonObject = JSONObject(jsonString)  // 그 후 JSONObject로 변환
                        println("으아아CheckIn22222 응답 바디: $jsonObject")  // JSON 객체 출력
                    } catch (e: Exception) {
                        println("JSON 파싱 오류: ${e.message}")
                    }
                } else {
                    println("응답이 비어 있습니다.")
                }
            } else {
                println("응답 실패: ${response.code()}")
            }










            if (response.isSuccessful) {
                val jsonString = response.body()?.string()

                if (jsonString != null) {
                    val jsonObject = JSONObject(jsonString)
                    if (jsonObject.has("code")) {
                        val code = jsonObject.optString("code") // 최상위 레벨에서 'code'를 추출
                        println("으아아CheckIn code: $code")

                    } else {
                        val name = jsonObject.optString("name") // "name" 키가 없을 경우
                        println("으아아CheckIn222"+ "Name: $name")

                    }
                }
            } else {
                println("으아아CheckIn"+"Error: ${response.code()}")
            }
        } catch (e: Exception) {
        }
    }
    try {
        val request = CheckInRequest(
            number = "2541",
            isSmsDisabled = "true"
        )
        val token = "Bearer qvvtRm3L8c0XHc46wphd7JgEg1dtNViOdRaEkVIRSns3URw8clGNuEiU2siB"

        val a= apiService.postCheckIn(
            token,
          request
        )

        println(a.toString())

    } catch (e: Exception) {
        println("오류 발생: ${e.message}")
    }*/
    }





    /*@Test
    fun testCheckIn(): Unit = runBlocking {
        val request = CheckInRequest(
            apiToken = "qvvtRm3L8c0XHc46wphd7JgEg1dtNViOdRaEkVIRSns3URw8clGNuEiU2siB",
            checkInId = "2174"
        )
        try {
            val a= apiService.postCheckIn(
                checkInId = request.checkInId.toString(),
                apiToken = request.apiToken
            )

            if (a.isSuccessful) {
                println("✅ 응답 코드: ${a.code()}")
            } else {
                println("❌ 실패 응답 코드: ${a.code()}")
            }


        } catch (e: Exception) {
            println("오류 발생: ${e.message}")
        }
    }*/




/*

    @Test
    fun testCheckIn2(): Unit = runBlocking {
        val request = CheckInRequest(
            apiToken = "qvvtRm3L8c0XHc46wphd7JgEg1dtNViOdRaEkVIRSns3URw8clGNuEiU2siB",
            checkInId = "2335"
        )
        try {
            val a= apiService.postCheckIn2(
                checkInRequest = request
            )

            if (a.isSuccessful) {
                println("✅ 응답 코드: ${a.code()}")
            } else {
                println("❌ 실패 응답 코드: ${a.code()}")
            }


        } catch (e: Exception) {
            println("오류 발생: ${e.message}")
        }
    }*/
