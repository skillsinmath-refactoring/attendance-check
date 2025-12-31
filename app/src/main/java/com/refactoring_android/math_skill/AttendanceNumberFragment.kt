package com.refactoring_android.math_skill

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.refactoring_android.math_skill.data.request.CheckInRequest
import com.refactoring_android.math_skill.data.response.ClassInfo
import com.refactoring_android.math_skill.databinding.FragmentAttendanceNumberBinding

class AttendanceNumberFragment : Fragment() {
    private val attendanceViewModel by activityViewModels<AttendanceViewModel>()
    private val binding: FragmentAttendanceNumberBinding by lazy {
        FragmentAttendanceNumberBinding.inflate(layoutInflater)
    }

    private fun getStudentIdOrNull(): String? {
        val n1 = binding.inputNumber1.number
        val n2 = binding.inputNumber2.number
        val n3 = binding.inputNumber3.number
        val n4 = binding.inputNumber4.number

        return if (n1 != null && n2 != null && n3 != null && n4 != null) {

            "$n1$n2$n3$n4"
        } else {
            binding.buttonCheckAttendance.text = "입실"
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val classSelectView = ClassSelectViewManager.getInstance(requireActivity())

        attendanceViewModel.currentTime.observe(viewLifecycleOwner, { time ->
            binding.timeTv.text = time
        })

        val buttons = listOf(
            binding.button1 to 1, binding.button2 to 2, binding.button3 to 3,
            binding.button4 to 4, binding.button5 to 5, binding.button6 to 6,
            binding.button7 to 7, binding.button8 to 8, binding.button9 to 9,
            binding.button0 to 0
        )
        binding.button1.setOnClickListener {
            appendNumberToInput(1)
        }

        buttons.forEach { (button, number) ->
            button.setOnClickListener {
                button.showPressEffect()
                appendNumberToInput(number)
                val studentId = getStudentIdOrNull() // 반환 값을 변수에 저장
                if (studentId != null) {
                    attendanceViewModel.checkIn(requireContext(), studentId)
                } else {
                    attendanceViewModel.isCheckIn.value = false
                }
            }
        }
        attendanceViewModel.checkInUser.observe(viewLifecycleOwner, { result ->
            if (result?.att == 1) {
                binding.buttonCheckAttendance.text = "퇴실"
            } else {
                binding.buttonCheckAttendance.text = "입실"
            }
        })

        attendanceViewModel.checkInResult.observe(viewLifecycleOwner, { result ->
            if (result) {
                var toastMessage = ""
                if (attendanceViewModel.checkInUser.value?.att == 1) {
                    toastMessage = "퇴실이 완료되었습니다."
                } else {
                    toastMessage = "입실이 완료되었습니다."
                }
                Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
            }

        })


        binding.buttonDelete.setOnClickListener {
            getStudentIdOrNull()
            clearLastInput()
            it.showPressEffect()
            classSelectView.hide()
            attendanceViewModel.isCheckIn.value = false
        }

        binding.buttonClear.setOnClickListener {
            for (i in 1..4) {
                clearLastInput()
            }
            it.showPressEffect()
            getStudentIdOrNull()
            classSelectView.hide()
            attendanceViewModel.isCheckIn.value = false
        }

        binding.buttonCheckAttendance.setOnClickListener {
            classSelectView.hide()
            attendanceViewModel.isCheckIn.value = false
            binding.buttonCheckAttendance.text = "입실"
            if (!(binding.inputNumber1.number != null && binding.inputNumber2.number != null && binding.inputNumber3.number != null && binding.inputNumber4.number != null)){
                return@setOnClickListener}
            val studentId =
                "${binding.inputNumber1.number}${binding.inputNumber2.number}${binding.inputNumber3.number}${binding.inputNumber4.number}"
            println("입실 버튼 클릭+"+studentId)
            val request = CheckInRequest(
                number = studentId,
                isSmsDisabled = "false",
                classNameId = if(attendanceViewModel.checkInUser.value?.att==0) classSelectView.getSelectedClassId() else null
            )
        /*    val analytics = Firebase.analytics

            val bundle = Bundle().apply {
                putString("studentId", studentId) // 클릭한 버튼 이름
                putString("class_id",
                    (if(attendanceViewModel.checkInUser.value?.att==0) classSelectView.getSelectedClassId() else null).toString()
                )         // (선택) 어떤 화면에서 클릭했는지
            }
            analytics.logEvent("button_click", bundle)*/

            println("출석요청청청" + request)
            attendanceViewModel.postCheckIn(
                requireContext(),
                request
            )
            attendanceViewModel.getCheckInList(
                requireContext(),
                attendanceViewModel.currentTime.value.toString()
            )

            for (i in 1..4) {
                clearLastInput()
            }
        }

        attendanceViewModel.isCheckIn.observe(viewLifecycleOwner, { isCheckIn ->


            if (!isCheckIn) {
                classSelectView.hide()
                binding.buttonCheckAttendance.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.pressedColor)
                )
                binding.buttonCheckAttendance.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.black)
                )
            }
            else{
                binding.buttonCheckAttendance.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.red2)
                )
                binding.buttonCheckAttendance.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
            }
        })

        return binding.root
    }

    private fun appendNumberToInput(number: Int) {
        when {
            binding.inputNumber1.number == null -> binding.inputNumber1.setValue(number)
            binding.inputNumber2.number == null -> binding.inputNumber2.setValue(number)
            binding.inputNumber3.number == null -> binding.inputNumber3.setValue(number)
            binding.inputNumber4.number == null -> binding.inputNumber4.setValue(number)
        }
    }

    private fun clearLastInput() {
        when {
            binding.inputNumber4.number != null -> binding.inputNumber4.setValue(null)
            binding.inputNumber3.number != null -> binding.inputNumber3.setValue(null)
            binding.inputNumber2.number != null -> binding.inputNumber2.setValue(null)
            binding.inputNumber1.number != null -> binding.inputNumber1.setValue(null)
        }
    }

    private fun bounceEffect(view: View) {
        view.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100)
            .withEndAction {
                view.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
            }.start()
    }

    fun View.showPressEffect(
        duration: Long = 100L
    ) {
        this.setBackgroundColor(ContextCompat.getColor(context, R.color.pressedColor))

        this.postDelayed({
            this.setBackgroundResource(com.refactoring_android.math_skill.core.R.drawable.number_button)
        }, duration)
    }

}
val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()
