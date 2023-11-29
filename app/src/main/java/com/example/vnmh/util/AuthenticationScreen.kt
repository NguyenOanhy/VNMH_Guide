package com.example.vnmh.util

import android.content.Context
import android.widget.Toast
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
import com.example.vnmh.util.FirebaseAuthManager

@Composable
fun AuthenticationScreen(onLoginSuccess: () -> Unit) {
    val context = LocalContext.current
    val emailState = remember { mutableStateOf(TextFieldValue()) }
    val passwordState = remember { mutableStateOf(TextFieldValue()) }

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
                // Đăng xuất
                FirebaseAuthManager.logout()
                Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
            }
        ) {
            Text("Log Out")
        }
    }
}

@Preview
@Composable
fun AuthenticationScreenPreview() {
    AuthenticationScreen {}
}