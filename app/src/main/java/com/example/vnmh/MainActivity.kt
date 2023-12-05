package com.example.vnmh

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.vnmh.composable.FeedbackBottomSheet
import com.example.vnmh.navigation.Navigation
import com.example.vnmh.ui.theme.MuseumAppTheme
import com.example.vnmh.util.ShakeDetector
import com.example.vnmh.composable.LoginScreen
import com.example.vnmh.composable.SignupScreen
import com.example.vnmh.navigation.UserState
import com.example.vnmh.navigation.UserStateViewModel
import com.example.vnmh.util.FirebaseAuthManager
import com.example.vnmh.viewModel.FavouriteViewModel
import com.example.vnmh.viewModel.MuseumViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MuseumViewModel>()
    private val favouriteViewModel by viewModels<FavouriteViewModel>()
    private var mSensorManager: SensorManager? = null
    private var mShakeDetector: ShakeDetector? = null

    private val userState by viewModels<UserStateViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mShakeDetector = ShakeDetector()

        installSplashScreen()
        setContent {
            CompositionLocalProvider(UserState provides userState) {
                ApplicationSwitcher()
            }

        }
    }

    val firestore = FirebaseFirestore.getInstance()

    private fun sendFeedbackToFirebase(feedbackContent: String, firestore: FirebaseFirestore) {
        // Kết nối với Firestore và tạo reference đến collection "Feedback"
        val feedbackCollection = firestore.collection("Feedback")

        // Tạo một document mới trong collection và đặt nội dung phản hồi
        val documentRef = feedbackCollection.document()
        documentRef.set(mapOf("feedback" to feedbackContent))
            .addOnSuccessListener {
                // Xử lý khi lưu thành công
                Log.d("FeedBack", "Feedback saved to Firestore successfully.")
            }
            .addOnFailureListener { e ->
                // Xử lý khi lưu thất bại
                Log.d("FeedBack", "Error saving feedback to Firestore: $e")
            }
    }



    @Composable
    fun ApplicationSwitcher() {
        val vm = UserState.current
        if (vm.isLoggedIn) {
            MuseumAppContent(viewModel, favouriteViewModel)
        } else {
            FirebaseAuthManager.logout()
            AppContent()
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun AppContent() {
        val isOnLoginScreen = remember { mutableStateOf(true) }
        MuseumAppTheme {
            if (isOnLoginScreen.value) {
                LoginScreen(
                    onLoginSuccess = {
                        isOnLoginScreen.value = false
                    },
                    onSignupClick = {
                        isOnLoginScreen.value = false
                    }
                )
            } else {
                SignupScreen(
                    onSignupSuccess = {
                        isOnLoginScreen.value = true
                    },
                    onLoginClick = {
                        isOnLoginScreen.value = true
                    }
                )
            }
        }
    }

    @Composable
    fun MuseumAppContent(viewModel: MuseumViewModel, favouriteViewModel: FavouriteViewModel) {
        val context = LocalContext.current
        Navigation(viewModel, favouriteViewModel)
        FeedbackBottomSheet(
            shakeDetector = mShakeDetector,
            onSendFeedback = { feedback ->
                sendFeedbackToFirebase(feedback, firestore = firestore)
                Toast.makeText(context, "Gửi phản hồi thành công!", Toast.LENGTH_SHORT).show()
//                    val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "feedback@android.com", null)).apply {
//                        putExtra(Intent.EXTRA_SUBJECT, "Museum App Feedback")
//                        putExtra(Intent.EXTRA_TEXT, feedback)
//                    }
//                    startActivity(emailIntent)
            },
        )
    }

    override fun onResume() {
        super.onResume()
        mSensorManager?.registerListener(
            mShakeDetector,
            mSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_UI
        )
    }

    override fun onPause() {
        mSensorManager?.unregisterListener(mShakeDetector)
        super.onPause()
    }
}