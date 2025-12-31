package com.refactoring_android.math_skill.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.refactoring_android.math_skill.data.response.UserInfoResponse
import com.refactoring_android.math_skill.data.network.NetworkService.retrofit
import com.refactoring_android.math_skill.data.network.api.MathSkillApi
import com.refactoring_android.math_skill.data.request.LoginRequest
import com.refactoring_android.math_skill.data.request.SocialLoginRequest
import com.refactoring_android.math_skill.domain.UserInfo
import com.refactoring_android.math_skill.extension.TokenDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(

) : ViewModel() {
    val isLogin: MutableLiveData<Boolean> = MutableLiveData()
    private val mathSkillApi = retrofit.create(MathSkillApi::class.java)


    fun clearToken(context: Context) {
        viewModelScope.launch {
            TokenDataStore.clearToken(context)
        }
    }

    fun userLogin(context: Context, email: String, password: String) {
        viewModelScope.launch {
            try {
               mathSkillApi.getLoginInfo(
                    LoginRequest(
                        email = email,
                        password = password
                    )
               ).let {
                   isLogin.value = true
                   Log.d("으아아123123",  it.user.apiToken)
                   TokenDataStore.saveToken(context, it.user.apiToken,it.user)
               }
            } catch (e: Exception) {
                isLogin.value = false
                Log.e("API_ERROR", "오류: ${e.message}")
            }
        }
    }
}