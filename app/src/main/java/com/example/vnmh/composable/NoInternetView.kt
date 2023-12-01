package com.example.vnmh.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoInternetPlaceholder() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Không có kết nối Internet.\nVui lòng kiểm tra kết nối của bạn và thử lại.",
            color = Color.Gray,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}
