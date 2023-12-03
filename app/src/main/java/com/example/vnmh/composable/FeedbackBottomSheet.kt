package com.example.vnmh.composable

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.vnmh.ui.theme.FeedBackColor
import com.example.vnmh.util.ShakeDetector
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackBottomSheet(shakeDetector: ShakeDetector?, onSendFeedback: (String) -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    var listState = rememberScrollState()

    var feedbackContent by remember { mutableStateOf(TextFieldValue()) }

    LaunchedEffect(key1 = shakeDetector) {
        shakeDetector?.setOnShakeListener {
            showBottomSheet = true
            if (!sheetState.isVisible) {
                scope.launch {
                    sheetState.show()
                }
            }
        }
    }

    val imeInsets = WindowInsets.ime
    val density = LocalDensity.current
    val keyboardHeightDp = with(density) { imeInsets.getBottom(this).toDp() }
    Log.d("Keyboard", "$keyboardHeightDp")

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FeedBackColor.backColor)
                    .verticalScroll(state = listState)
                    .padding(16.dp, 16.dp, 16.dp, 300.dp + keyboardHeightDp)
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(10.dp)
            ) {
                Text(text = "Phản hồi", style = MaterialTheme.typography.headlineMedium)

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = feedbackContent,
                    onValueChange = { feedbackContent = it },
                    label = { Text("Hãy cho chúng tôi biết về trải nghiệm của bạn", color = FeedBackColor.mainColor) },
                    modifier = Modifier.fillMaxWidth().height(200.dp).background(FeedBackColor.backColor),
                    maxLines = 5,
                    colors = TextFieldDefaults.textFieldColors(containerColor = FeedBackColor.fieldColor),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        },
                        border = BorderStroke(1.dp, FeedBackColor.mainColor),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = FeedBackColor.mainColor)
                    ) {
                        Text(text = "Bỏ", color = FeedBackColor.mainColor)
                    }

                    Button(onClick = {
                            val feedbackContent = feedbackContent.text
                            onSendFeedback(feedbackContent)
                            scope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(FeedBackColor.mainColor)
                    ) {
                        Text("Gửi", color = Color.White)
                    }
                }
            }
        }
    }
}