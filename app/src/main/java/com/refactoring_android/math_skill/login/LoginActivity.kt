package com.refactoring_android.math_skill.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.lifecycleScope
import com.refactoring_android.math_skill.MainActivity
import com.refactoring_android.math_skill.R
import com.refactoring_android.math_skill.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel = LoginViewModel()

    private val loginLauncher =
        this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
            } else {
                Toast.makeText(this, "인증 정보가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.naverLoginButton.setOnClickListener {
            Toast.makeText(this, "추후 업데이트 예정입니다.", Toast.LENGTH_SHORT).show()
        }
        initViewModel()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginScreen)) { v, insets ->
            v.setPadding(0, 0, 0, 0)
            insets
        }

        enableEdgeToEdge()
        initView()
    }


    private fun initView() = with(binding) {


        loginButton.setOnClickListener {
            if (binding.emailInput.text.isNotEmpty() && binding.passwordInput.text.isNotEmpty())
                login()
            else
                Toast.makeText(this@LoginActivity, "이메일과 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
        }
        /*  naverLoginButton.setOnClickListener {
              naverLogin()
          }*/
    }


    private fun login() {
        lifecycleScope.launch {
            viewModel.userLogin(
                this@LoginActivity,
                binding.emailInput.text.toString(),
                binding.passwordInput.text.toString()
            )
        }
    }

    private fun initViewModel() {
        viewModel.isLogin.observe(this) { isLogin ->
            Log.e("로그인으아아", "isLogin: $isLogin")
            if (isLogin == true) {
                val intent = Intent(this, MainActivity::class.java)
                loginLauncher.launch(intent)

            } else {
                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

}