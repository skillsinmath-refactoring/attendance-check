package com.refactoring_android.math_skill

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.annotations.SerializedName
import com.refactoring_android.math_skill.data.network.NetworkService.retrofit
import com.refactoring_android.math_skill.data.network.api.MathSkillApi
import com.refactoring_android.math_skill.data.request.CheckInRequest
import com.refactoring_android.math_skill.data.response.Attendance
import com.refactoring_android.math_skill.data.response.ClassInfo
import com.refactoring_android.math_skill.data.response.User
import com.refactoring_android.math_skill.domain.StudentInfo
import com.refactoring_android.math_skill.domain.UserInfo
import com.refactoring_android.math_skill.domain.util.toSeconds
import com.refactoring_android.math_skill.domain.util.toTimeFormat
import com.refactoring_android.math_skill.extension.TokenDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class AttendanceViewModel @Inject constructor(

) : ViewModel() {
    private val _studentList: MutableStateFlow<List<StudentInfo>> = MutableStateFlow(emptyList())
    val studentList: StateFlow<List<StudentInfo>> = _studentList.asStateFlow()
    private val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    private val _currentTime = MutableLiveData(getCurrentTime())
    val currentTime: LiveData<String> = _currentTime
    val isCheckIn: MutableLiveData<Boolean> = MutableLiveData(false)
    val exception: MutableLiveData<Throwable?> = MutableLiveData()
    val checkInError: MutableLiveData<String> = MutableLiveData()
    val checkInResult : MutableLiveData<Boolean> = MutableLiveData(false)
    private val mathSkillApi = retrofit.create(MathSkillApi::class.java)

    private val _checkInUser = MutableLiveData<UserInfo?>()
    val checkInUser: LiveData<UserInfo?> = _checkInUser

    var studentName: String? = null
    private val _checkInList = MutableLiveData<List<Attendance>?>()
    val checkInList: LiveData<List<Attendance>?> = _checkInList
    private val _top3List = MutableLiveData<List<Attendance>?>()
    val top3List: LiveData<List<Attendance>?> = _top3List

    private val _attendanceCount = MutableLiveData<Int>()
    val attendanceCount: LiveData<Int> get() = _attendanceCount

    private val _studyingCount = MutableLiveData<Int>()
    val studyingCount: LiveData<Int> get() = _studyingCount

    val isLoading = MutableLiveData(false)

    private val _classList = MutableLiveData<List<ClassInfo>?>()
    val classList: LiveData<List<ClassInfo>?> = _classList

    var dialogVisibility = MutableLiveData<Boolean>(false)

    val combinedData = MediatorLiveData<Pair<Int?, Int?>>().apply {
        addSource(attendanceCount) { attendance ->
            val studying = studyingCount.value
            value = attendance to studying
        }

        addSource(studyingCount) { studying ->
            val attendance = attendanceCount.value
            value = attendance to studying
        }
    }
    init {
        viewModelScope.launch {
            while (isActive) {
                _currentTime.postValue(getCurrentTime())
                delay(1000)
            }
        }


    }

    private fun getCurrentTime(): String {
        return dateFormat.format(Date())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkIn(context: Context, checkInId: String) { // 출석번호 유효한지 확인
        viewModelScope.launch {
            val token = TokenDataStore.getToken(context)
            isLoading.value = true
            exception.value = null
            try {
                val response = mathSkillApi.getCheckIn(checkInId, token)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.code == 200 && body.data != null) {
                        val user = body.data
                        user?.classes?.forEach { classInfo ->
                            val todayDayOfWeek = LocalDate.now().dayOfWeek.value % 7
                            
                            val todayClasses = user.classes.filter {
                                it.day == todayDayOfWeek
                            }
                            _classList.value = todayClasses
                        }
                        _checkInUser.value = UserInfo(
                            apiToken = user?.apiToken ?: "",
                            id = user?.id ?: 0,
                            name = user?.name ?: "",
                            att = user?.isAtSugi ?: 0,
                            type = user?.type ?: "",
                            roleId = user?.roleId ?: 0,)
                        studentName = user?.name
                        isCheckIn.value = true
                    } else {
                        studentName = body?.data?.name ?: "학생 없음"
                    }

                } else {
                    exception.value = Exception("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                exception.value = e
            }
            isLoading.value = false
        }
    }

    fun getCheckInList(context: Context, date: String) {//출석 목록 불러오기
        viewModelScope.launch {
            isLoading.value = true
            try {
                val token = TokenDataStore.getToken(context)
                val response = mathSkillApi.getCheckInList(
                    apiToken = token?:"",
                    date = date
                )

                if(response.code == 200){
                    _checkInList.value = response.userData?.data?.map { it.copy() }

                     _top3List.value = response.userData?.top3?.toList()
                    _attendanceCount.value = response.userData?.attendanceCount ?: 0
                    _studyingCount.value = response.userData?.studyingCount ?: 0
                }
            } catch (e: Exception) {
                Log.e("출석오류API_ERROR", "오류: ${e.message}")
                Toast.makeText(context, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
            isLoading.value = false
        }
    }


    fun postCheckIn(context: Context, checkInRequest: CheckInRequest) { //출석체크
        viewModelScope.launch {
            isLoading.value = true

            val token = TokenDataStore.getToken(context)
            try {
                val response = mathSkillApi.postCheckIn(
                    "Bearer $token",
                    checkInRequest
                )
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        val jsonString = responseBody.string()
                        Log.d("CheckIn응답", "JSON: $jsonString") // 전체 JSON 확인
                        checkInResult.value = true
                    }

                } else {
                    checkInError.value = "서버에서 오류가 발생했습니다."
                }
                delay(500)
            } catch (e: Exception) {
                checkInError.value = "알 수 없는 오류가 발생했습니다."
            }
            isLoading.value = false

            getCheckInList(
                context,
                currentTime.value.toString()
            ) /*// 출석 체크 후 출석 목록 업데이트*/
        }
    }
}

