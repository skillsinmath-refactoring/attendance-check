package com.refactoring_android.math_skill

import android.annotation.SuppressLint
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.refactoring_android.math_skill.databinding.ActivityMainBinding
import com.refactoring_android.math_skill.studentList.StudentListFragment
import com.refactoring_android.math_skill.util.isTablet
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    private var nfcAdapter: NfcAdapter? = null
    private lateinit var binding: ActivityMainBinding
    private val viewModel: AttendanceViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @SuppressLint("NewApi")
        //viewModel.getCheckInList(this@MainActivity, today) // 날짜는 현재 날짜로 설정
        val classSelectView = ClassSelectViewManager.getInstance(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        (classSelectView.parent as? ViewGroup)?.removeView(classSelectView)
        binding.classSelectContainer.addView(classSelectView)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT


        nfcAdapter = NfcAdapter.getDefaultAdapter(this@MainActivity);
        if (savedInstanceState == null) {
            val studentListFragment = StudentListFragment()
            val attendanceNumberFragment = AttendanceNumberFragment()

            supportFragmentManager.beginTransaction()
                .replace(R.id.attendanceNumber, attendanceNumberFragment)
                .commit()

            supportFragmentManager.beginTransaction()
                .replace(R.id.studentList, studentListFragment)
                .commit()
        }

        viewModel.checkInUser.observe(this, { result ->
           if(result?.att == 0 && result?.roleId == 2) {//아이디가 학생이고, 입실할 때 클래스 선택 창 보여주기
                val newWidth = if (this.isTablet()) 500.dp else 200.dp
                binding.classSelectContainer.layoutParams =
                    binding.classSelectContainer.layoutParams?.apply {
                        height = newWidth * 4 / 5
                    }
                val classList = viewModel.classList.value
                classSelectView.setStudentName(result?.name ?: "")
                classSelectView.bindClassList(classList ?: emptyList())
                classSelectView.show()
            }
        })

        onBackPressedDispatcher.addCallback(this) {
            finishAffinity() // 전체 앱 종료
        }


        viewModel.isLoading.observe(this, { isLoading ->
            if (isLoading) {
                this.window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
                this.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        })
        initViewModel()
    }

    private fun initNfc() {
        if (nfcAdapter == null) {
            // NFC를 지원하지 않는 디바이스
            return
        } else if (nfcAdapter?.isEnabled == false) {
            // 사용자에게 활성화를 요청
            val intent = Intent(Settings.ACTION_NFC_SETTINGS)
            startActivity(intent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        intent?.action = NfcAdapter.ACTION_TAG_DISCOVERED
        super.onNewIntent(intent)
    }


    private fun initViewModel() {
        viewModel.checkInError.observe(this, { error ->
           Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }


}


data class AndroidTestResponse(
    val result: String
)