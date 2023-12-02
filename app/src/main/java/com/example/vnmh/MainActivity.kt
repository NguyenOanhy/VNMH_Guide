package com.example.vnmh

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.vnmh.composable.FeedbackBottomSheet
import com.example.vnmh.navigation.Navigation
import com.example.vnmh.ui.theme.MuseumAppTheme
import com.example.vnmh.util.ShakeDetector
import com.example.vnmh.composable.LoginScreen
import com.example.vnmh.composable.SignupScreen
import com.example.vnmh.viewModel.FavouriteViewModel
import com.example.vnmh.viewModel.MuseumViewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MuseumViewModel>()
    private val favouriteViewModel by viewModels<FavouriteViewModel>()
    private var mSensorManager: SensorManager? = null
    private var mShakeDetector: ShakeDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mShakeDetector = ShakeDetector()

        setContent {
            AppContent(viewModel, favouriteViewModel)
        }
    }

    @Composable
    fun AppContent(viewModel: MuseumViewModel, favouriteViewModel: FavouriteViewModel) {
        val isOnLoginScreen = remember { mutableStateOf(true) }
        MuseumAppTheme {
            if (isOnLoginScreen.value) {
                LoginScreen(
                    onLoginSuccess = {
                        isOnLoginScreen.value = false
                        setContent {
                            MuseumAppContent(viewModel, favouriteViewModel)
                        }
                    },
                    onSignupClick = {
                        isOnLoginScreen.value = false
                    }
                )
            } else {
                SignupScreen(
                    onSignupSuccess = {
                        isOnLoginScreen.value = true
                        setContent {
                            MuseumAppContent(viewModel, favouriteViewModel)
                        }
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
        Navigation(viewModel, favouriteViewModel)
        FeedbackBottomSheet(
                shakeDetector = mShakeDetector,
                onSendFeedback = { feedback ->
                    val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "ivan@metropolia.fi", null)).apply {
                        putExtra(Intent.EXTRA_SUBJECT, "Museum App Feedback")
                        putExtra(Intent.EXTRA_TEXT, feedback)
                    }
                    startActivity(emailIntent)
                }
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