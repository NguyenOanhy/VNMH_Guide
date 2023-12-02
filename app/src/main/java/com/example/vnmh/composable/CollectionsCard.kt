package com.example.vnmh.composable

import android.widget.Toast
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.vnmh.util.FirebaseAuthManager
import com.example.vnmh.viewModel.MuseumViewModel
import kotlinx.coroutines.delay

@Composable
fun CollectionsCard(
    navController: NavController,
    viewModel: MuseumViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    var currentIconIndex by remember { mutableIntStateOf(0) }
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (showDialog) {
        InfoDialog(onDismiss = { showDialog = false })
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Log Out",
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    // Xử lý logic khi nhấp vào "Log Out"
                    // Chuyển sang màn hình đăng nhập
                    // Ví dụ: navigateToLoginScreen()
                }
        )
        Icon(
            modifier = Modifier
                .align(Alignment.End)
                .padding(16.dp)
                .size(30.dp)
                .clip(CircleShape)
                .background(
                    color = Color.Transparent,
                    shape = CircleShape,
                )
                .clickable { showDialog = true },
            imageVector = Icons.Outlined.Info,
            contentDescription = "Info",
            tint = MaterialTheme.colorScheme.onBackground
        )
        if(!isLandscape) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp, 150.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Bảo tàng",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 0.dp, 0.dp, 0.dp),
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Lịch sử Quốc gia",
                        modifier = Modifier
                            .padding(0.dp, 0.dp, 0.dp, 0.dp),
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Bảo tàng Lịch sử Quốc gia",
                    modifier = Modifier
                        .padding(50.dp, 120.dp, 0.dp, 0.dp),
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}