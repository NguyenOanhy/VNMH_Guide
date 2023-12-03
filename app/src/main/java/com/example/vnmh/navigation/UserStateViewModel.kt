package com.example.vnmh.navigation

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.vnmh.util.FirebaseAuthManager

class UserStateViewModel : ViewModel() {
    var isLoggedIn by mutableStateOf(false)
    suspend fun signIn() {
        isLoggedIn = true
    }
    suspend fun signOut() {
        FirebaseAuthManager.logout()
        isLoggedIn = false
    }
}

val UserState = compositionLocalOf<UserStateViewModel> { error("User State Context Not Found!") }

