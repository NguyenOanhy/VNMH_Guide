package com.example.vnmh.composable

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vnmh.util.FirebaseAuthManager
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun SignupScreen(onSignupSuccess: () -> Unit) {
    val context = LocalContext.current
    val emailState = remember { mutableStateOf(TextFieldValue()) }
    val passwordState = remember { mutableStateOf(TextFieldValue()) }
    val confirmPasswordState = remember { mutableStateOf(TextFieldValue()) }
    val userName = remember { mutableStateOf(TextFieldValue()) }
    val phoneNumber = remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            modifier = Modifier.padding(16.dp),
            value = userName.value,
            onValueChange = { userName.value = it },
            label = { Text("User name") }
        )

        OutlinedTextField(
            modifier = Modifier.padding(16.dp),
            value = phoneNumber.value,
            onValueChange = { phoneNumber.value = it },
            label = { Text("Phone Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

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

        OutlinedTextField(
            modifier = Modifier.padding(16.dp),
            value = confirmPasswordState.value,
            onValueChange = { confirmPasswordState.value = it },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Confirm Password") }
        )

        Button(
            modifier = Modifier.padding(16.dp),
            onClick = {
                val email = emailState.value.text
                val password = passwordState.value.text
                val confirmPassword = confirmPasswordState.value.text

                if (password == confirmPassword) {
                    // Đăng ký
                    FirebaseAuthManager.signUp(email, password) { success, errorMessage ->
                        if (success) {
                            // Save user information to Firestore collection
                            val user = hashMapOf(
                                "username" to userName.value.text,
                                "phone" to phoneNumber.value.text,
                                "email" to email
                            )

                            val firestore = FirebaseFirestore.getInstance()
                            firestore.collection("USERS")
                                .add(user)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Signed up successfully", Toast.LENGTH_SHORT).show()
                                    onSignupSuccess.invoke()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Signup failed: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            // Đăng ký thất bại
                            Toast.makeText(context, "Signup failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // Hiển thị thông báo khi mật khẩu không khớp
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text("Sign Up")
        }

    }
}

@Preview
@Composable
fun SignupScreenPreview() {
    SignupScreen ({})
}