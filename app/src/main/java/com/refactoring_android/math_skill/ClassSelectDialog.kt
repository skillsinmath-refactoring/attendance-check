package com.refactoring_android.math_skill.view

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.refactoring_android.math_skill.ClassSelectAdapter
import com.refactoring_android.math_skill.data.response.ClassInfo
import com.refactoring_android.math_skill.databinding.BottomSheetDialogBinding

class ClassSelectView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val binding = BottomSheetDialogBinding.inflate(LayoutInflater.from(context), this, true)
    private var adapter: ClassSelectAdapter? = null
    private var studentName: String? = null

    init {
        orientation = VERTICAL
        visibility = GONE
        binding.classRecyclerView.apply {
            isVerticalScrollBarEnabled = true    // 세로 스크롤바 켜기
            isScrollbarFadingEnabled = false      // 스크롤바 사라지지 않게
        }
        applySizeForDevice()
    }

    fun setStudentName(name: String) {
        studentName = name
        val nameWithJosa = getNameWithJosa(name)
        binding.tvMessage.text = "안녕, ${nameWithJosa}!\n오늘도 열공해서 이번달 학습시간 1등하자! 파이팅!"

    }

    fun bindClassList(classList: List<ClassInfo>) {
        adapter = ClassSelectAdapter(classList) { selectedClass ->
        }
        binding.classRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.classRecyclerView.adapter = adapter
    }

    fun getSelectedClassId(): Int? {
        return adapter?.getSelectedClass()?.id
    }

    fun show() {
        visibility = VISIBLE
    }

    fun hide() {
        visibility = GONE
    }

    fun toggle() {
        visibility = if (visibility == VISIBLE) GONE else VISIBLE
    }



    private fun applySizeForDevice() {
        val isTablet = (resources.configuration.screenLayout
                and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE

        val heightDp = if (isTablet) LayoutParams.WRAP_CONTENT else 150.dp
        // 뷰 전체 너비 조절
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, heightDp)

        // 타이틀 폰트 크기도 태블릿에서 키움
        binding.tvTitle.setTextSize(
            TypedValue.COMPLEX_UNIT_SP,
            if (isTablet) 25f else 13f
        )
        binding.tvClassTitle.setTextSize(
            TypedValue.COMPLEX_UNIT_SP,
            if (isTablet) 25f else 13f
        )
    }

    // 확장 함수로 dp 변환
    private val Int.dp: Int
        get() = (this * resources.displayMetrics.density).toInt()
}
fun getNameWithJosa(name: String): String {
    val trimmed = name.trim()
    if (trimmed.length < 2) return trimmed // 성만 있는 경우 그대로 반환

    val firstName = trimmed.substring(1)
    if (firstName.isEmpty()) return trimmed // 이름이 없으면 그대로 반환

    val lastChar = firstName.lastOrNull() ?: return trimmed // 방어적으로 체크
    val code = lastChar.code

    val hasJongseong = if (code in 0xAC00..0xD7A3) {
        val jongseong = (code - 0xAC00) % 28
        jongseong != 0
    } else {
        false
    }

    return if (hasJongseong) "${firstName}아" else "${firstName}야"
}
