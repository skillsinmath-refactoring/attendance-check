package com.refactoring_android.math_skill.studentList

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.color
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.refactoring_android.math_skill.AttendanceViewModel
import com.refactoring_android.math_skill.R
import com.refactoring_android.math_skill.databinding.FragmentStudentListBinding
import com.refactoring_android.math_skill.dp
import com.refactoring_android.math_skill.extension.TokenDataStore.getInfo
import com.refactoring_android.math_skill.login.LoginActivity
import com.refactoring_android.math_skill.login.LoginViewModel
import com.refactoring_android.math_skill.util.isTablet
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class StudentListFragment : Fragment() {

    private val studentListAdapter = StudentListAdapter()
    private val top3Adapter = Top3Adapter()
    private val loginViewModel: LoginViewModel by activityViewModels()
    private val attendanceViewModel by activityViewModels<AttendanceViewModel>()

    private val binding: FragmentStudentListBinding by lazy {
        FragmentStudentListBinding.inflate(layoutInflater)
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val today: String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        attendanceViewModel.getCheckInList(requireContext(), today)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.studentRv.adapter = studentListAdapter
        binding.top3Rv.adapter = top3Adapter
        binding.top3Rv.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )
        initView()
        initViewModel()
        return binding.root
    }

    private fun initView() {
        logoutMenu()

        val fullText = "금일 학습시간 TOP 3"
        val spannable = SpannableString(fullText)
        val start = fullText.indexOf("TOP 3")
        val end = start + "TOP 3".length

        spannable.setSpan(
            ForegroundColorSpan(Color.RED),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.top3Student.text = spannable
        lifecycleScope.launch {
            val branchName = getInfo(requireContext())
            binding.branchNameTv.text = branchName
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                attendanceViewModel.getCheckInList(
                    requireContext(),
                    attendanceViewModel.currentTime.value.toString()
                )
            }
        }

    }


    private fun initViewModel() {
        lifecycleScope.launch {
            attendanceViewModel.combinedData.observe(viewLifecycleOwner, { (attendance, studying) ->
                binding.currentStudentTv.text = SpannableStringBuilder()
                    .color(Color.RED) { append("$attendance") }
                    .append("명이 왔고 ")
                    .color((Color.RED)) { append("$studying") }
                    .append("명이 공부중")
            })
        }

        lifecycleScope.launch {
            attendanceViewModel.top3List.observe(viewLifecycleOwner, {
                top3Adapter.submitList(it)
            })
        }

        attendanceViewModel.checkInList.observe(viewLifecycleOwner, { checkInList ->
            val safeList = checkInList ?: emptyList()
            studentListAdapter.submitList(safeList) {
                if (safeList.isNotEmpty()) {
                    binding.studentRv.scrollToPosition(0)
                }
            }
        }
        )
        val cardView =
            layoutInflater.inflate(R.layout.attendance_card_view, binding.root, false)
        val tvGreeting = cardView.findViewById<TextView>(R.id.tv_greeting)
        val tvMessage = cardView.findViewById<TextView>(R.id.tv_message)
        val tvFighting = cardView.findViewById<TextView>(R.id.tv_fighting)
        val cardViewLayout = cardView.findViewById<View>(R.id.cardView)
        attendanceViewModel.isCheckIn.observe(viewLifecycleOwner, { isCheckIn ->
            if (!requireContext().isTablet()) {
                tvGreeting.textSize = 14f
                tvMessage.textSize = 12f
                val layoutParams = cardViewLayout.layoutParams
                layoutParams.height = 125.dp
                cardViewLayout.layoutParams = layoutParams
                tvFighting.textSize = 12f
            }
            tvGreeting.text = "안녕하세요! ${attendanceViewModel.studentName}! 수학의기술입니다!"
            if (!isCheckIn) {
                if (cardView?.parent != null) {
                    (cardView.parent as? ViewGroup)?.removeView(cardView)
                }

            } else if (attendanceViewModel.studentName != "error" && attendanceViewModel.checkInUser.value?.roleId == 1) {
                if (cardView?.parent == null) {
                    binding.root.addView(cardView)
                }

            }
        })

        attendanceViewModel.exception.observe(viewLifecycleOwner, { exception ->
            exception?.let {
                Toast.makeText(requireContext(), "존재하지 않는 출석 번호입니다.", Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun logoutMenu() {
        val toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.munu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_logout -> {
                        // NaverIdLoginSDK.logout()
                        loginViewModel.clearToken(requireContext())
                        Toast.makeText(requireContext(), "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                        true
                    }

                    R.id.action_withdrawal -> {
                        Toast.makeText(
                            requireContext(),
                            "회원 탈퇴를 하시려면 joe@navycode.co.kr로 문의 주세요",
                            Toast.LENGTH_LONG
                        ).show()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }


    fun String?.toSeconds(): Int =
        this?.split(":")?.map { it.toIntOrNull() ?: 0 }?.let {
            it[0] * 3600 + it[1] * 60 + it[2]
        } ?: 0

    fun Int.toTimeString(): String =
        String.format("%02d:%02d:%02d", this / 3600, (this % 3600) / 60, this % 60)

}