package com.example.vnmh.composable

import android.view.KeyEvent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vnmh.util.FirebaseAuthManager
import com.google.firebase.firestore.FirebaseFirestore

@ExperimentalComposeUiApi
@Composable
fun SignupScreen(onSignupSuccess: () -> Unit, onLoginClick: () -> Unit) {
    val context = LocalContext.current
    val emailState = remember { mutableStateOf(TextFieldValue()) }
    val passwordState = remember { mutableStateOf(TextFieldValue()) }
    val confirmPasswordState = remember { mutableStateOf(TextFieldValue()) }
    val userName = remember { mutableStateOf(TextFieldValue()) }
    val phoneNumber = remember { mutableStateOf(TextFieldValue()) }

    val (focusRequester1, focusRequester2, focusRequester3, focusRequester4, focusRequester5) = FocusRequester.createRefs()

    Column(
        modifier = Modifier
            .fillMaxSize().padding(top = 40.dp),
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
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        OutlinedTextField(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .onKeyEvent {
                    if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                        focusRequester1.requestFocus()
                        true
                    }
                    false
                },
            value = userName.value,
            onValueChange = { userName.value = it },
            label = { Text("Tên người dùng") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onNext = { focusRequester1.requestFocus() }
            ),
        )

        OutlinedTextField(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .focusRequester(focusRequester1)
                .onKeyEvent {
                    if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER){
                        focusRequester2.requestFocus()
                        true
                    }
                    false
                },
            value = phoneNumber.value,
            onValueChange = { phoneNumber.value = it },
            label = { Text("Số điện thoại") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            singleLine = true,
            keyboardActions = KeyboardActions(
                onNext = { focusRequester2.requestFocus() }
            ),
        )

        OutlinedTextField(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .focusRequester(focusRequester2)
                .onKeyEvent {
                    if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER){
                        focusRequester3.requestFocus()
                        true
                    }
                    false
                },
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            singleLine = true,
            keyboardActions = KeyboardActions(
                onNext = { focusRequester3.requestFocus() }
            ),
        )

        OutlinedTextField(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .focusRequester(focusRequester3)
                .onKeyEvent {
                    if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER){
                        focusRequester4.requestFocus()
                        true
                    }
                    false
                },
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Mật khẩu") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            singleLine = true,
            keyboardActions = KeyboardActions(
                onNext = { focusRequester4.requestFocus() }
            ),
        )

        OutlinedTextField(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .focusRequester(focusRequester4)
                .onKeyEvent {
                    if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER){
                        focusRequester5.requestFocus()
                        true
                    }
                    false
                },
            value = confirmPasswordState.value,
            onValueChange = { confirmPasswordState.value = it },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Nhập lại mật khẩu") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            singleLine = true,
            keyboardActions = KeyboardActions(
                onNext = { focusRequester5.requestFocus() }
            ),
        )

        Button(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .fillMaxWidth()
                .focusRequester(focusRequester5),
            onClick = {
                val name = userName.value.text
                val email = emailState.value.text
                val password = passwordState.value.text
                val confirmPassword = confirmPasswordState.value.text
                val phone = phoneNumber.value.text

                if (name == "" || email == "" || password == "" || confirmPassword == "" || phone == "") {
                    Toast.makeText(context, "Vui lòng điền đủ thông tin", Toast.LENGTH_SHORT).show()
                    return@Button
                } else {
                    val phoneRegex = Regex("^\\d{10}\$")
                    if (!phoneRegex.matches(phone)) {
                        // Show Toast if the phone number is not in the correct format
                        Toast.makeText(context, "Số điện thoại không đúng định dạng", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]\$")
                    if (!emailRegex.matches(email)) {
                        // Show Toast if the email is not in the correct format
                        Toast.makeText(context, "Email không đúng định dạng", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (password.length < 6) {
                        Toast.makeText(context, "Mật khẩu phải nhiều hơn 6 ký tự", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (password == confirmPassword) {
                        // Đăng ký
                        FirebaseAuthManager.signUp(email, password) { success, errorMessage ->
                            if (success) {
                                // Lưu thông tin người dùng vào Firestore
                                val user = hashMapOf(
                                    "username" to userName.value.text,
                                    "phone" to phoneNumber.value.text,
                                    "email" to email
                                )

                                val firestore = FirebaseFirestore.getInstance()
                                firestore.collection("USERS")
                                    .add(user)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            context,
                                            "Đăng ký thành công",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        onSignupSuccess.invoke()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(
                                            context,
                                            "Đăng ký thất bại: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            } else {
                                // Đăng ký thất bại
                                Toast.makeText(context, "Đăng ký thất bại", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    } else {
                        // Hiển thị thông báo khi mật khẩu không khớp
                        Toast.makeText(context, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show()
                    }
                }



            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF795548)), // Màu nâu
        ) {
            Text("Đăng ký", color = Color.White)
        }

        Text(
            modifier = Modifier
                .clickable {
                    onLoginClick.invoke() // Chuyển sang màn hình đăng ký
                },
            text = "Nếu đã có tài khoản, hãy đăng nhập",
            color = Color(0xFF795548),
            textDecoration = TextDecoration.Underline
        )
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun SignupScreenPreview() {
    SignupScreen ({}, {})
}