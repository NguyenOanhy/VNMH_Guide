package com.example.vnmh.composable

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings.Global.putString
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import com.example.vnmh.util.FirebaseAuthManager

private const val SHARED_PREFS_NAME = "LoginPrefs"
private const val KEY_EMAIL = "email"
private const val KEY_PASSWORD = "password"

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onSignupClick: () -> Unit) {
    val context = LocalContext.current
    val emailState = remember { mutableStateOf(TextFieldValue()) }
    val passwordState = remember { mutableStateOf(TextFieldValue()) }
    val sharedPreferences = remember { context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE) }

    // Lấy thông tin đăng nhập từ SharedPreferences khi màn hình được khởi tạo
    val savedEmail = sharedPreferences.getString(KEY_EMAIL, "")
    val savedPassword = sharedPreferences.getString(KEY_PASSWORD, "")
    if (!savedEmail.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
        emailState.value = TextFieldValue(savedEmail)
        passwordState.value = TextFieldValue(savedPassword)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            modifier = Modifier.padding(16.dp),
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = { Text("Email") }
        )

        OutlinedTextField(
            modifier = Modifier.padding(16.dp),
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Password") }
        )

        Button(
            modifier = Modifier.padding(16.dp),
            onClick = {
                val email = emailState.value.text
                val password = passwordState.value.text

                // Đăng nhập
                FirebaseAuthManager.login(email, password) { success, errorMessage ->
                    if (success) {
                        // Đăng nhập thành công
                        Toast.makeText(context, "Logged in successfully", Toast.LENGTH_SHORT).show()

                        // Lưu thông tin đăng nhập vào SharedPreferences
                        sharedPreferences.edit {
                            putString(KEY_EMAIL, email)
                            putString(KEY_PASSWORD, password)
                        }

                        onLoginSuccess.invoke() // Gọi lại callback khi đăng nhập thành công
                    } else {
                        // Đăng nhập thất bại
                        Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        ) {
            Text("Log In")
        }

        Button(
            modifier = Modifier.padding(16.dp),
            onClick = {
                onSignupClick.invoke() // Chuyển sang màn hình đăng ký
            }
        ) {
            Text("Sign Up")
        }
    }
}


@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen({}, {})
}