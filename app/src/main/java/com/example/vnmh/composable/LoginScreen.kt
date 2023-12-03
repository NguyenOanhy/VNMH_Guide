package com.example.vnmh.composable

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import com.example.vnmh.navigation.UserState
import com.example.vnmh.util.FirebaseAuthManager
import kotlinx.coroutines.launch

private const val SHARED_PREFS_NAME = "LoginPrefs"
private const val KEY_EMAIL = "email"
private const val KEY_PASSWORD = "password"

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onSignupClick: () -> Unit) {

    val vm = UserState.current

    val context = LocalContext.current
    val emailState = remember { mutableStateOf(TextFieldValue("")) }
    val passwordState = remember { mutableStateOf(TextFieldValue("")) }
    val sharedPreferences = remember { context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE) }

    // Lấy thông tin đăng nhập từ SharedPreferences khi màn hình được khởi tạo
    val savedEmail = sharedPreferences.getString(KEY_EMAIL, "")
    val savedPassword = sharedPreferences.getString(KEY_PASSWORD, "")

    // Sử dụng LaunchedEffect để gán giá trị ban đầu khi màn hình được khởi tạo
    LaunchedEffect(Unit) {
        emailState.value = TextFieldValue(savedEmail ?: "")
        passwordState.value = TextFieldValue(savedPassword ?: "")
    }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize().padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "VNMH Guide",
            color = Color(0xFF795548),
            style = MaterialTheme.typography.h1.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                textAlign = TextAlign.Center
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            value = emailState.value,
            onValueChange = { value ->
                emailState.value = value
            },
            label = { Text("Email") }
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            value = passwordState.value,
            onValueChange = { value ->
                passwordState.value = value
            },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Mật khẩu") }
        )

        Button(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            onClick = {
                val email = emailState.value.text
                val password = passwordState.value.text

                coroutineScope.launch {
                    vm.signIn()
                }

                // Đăng nhập
                FirebaseAuthManager.login(email, password) { success, errorMessage ->
                    if (success) {
                        Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                        // Lưu thông tin đăng nhập vào SharedPreferences
                        sharedPreferences.edit {
                            putString(KEY_EMAIL, email)
                            putString(KEY_PASSWORD, password)
                        }

                        onLoginSuccess.invoke() // Gọi lại callback khi đăng nhập thành công
                    } else {
                        // Đăng nhập thất bại
                        Toast.makeText(context, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF795548)), // Màu nâu
        ) {
            Text("Đăng nhập", color = Color.White)
        }

        Text(
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    onSignupClick.invoke() // Chuyển sang màn hình đăng ký
                },
            text = "Nếu chưa có tài khoản, hãy đăng ký",
            color = Color(0xFF795548),
            textDecoration = TextDecoration.Underline
        )
    }
}


@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen({}, {})
}