package com.example.vnmh.composable

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.vnmh.viewModel.MuseumViewModel
import kotlinx.coroutines.delay

@Composable
fun CollectionsCard(
    navController: NavController,
    viewModel: MuseumViewModel,
    selectedCard: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var currentIconIndex by remember { mutableIntStateOf(0) }

    if (showDialog) {
        InfoDialog(onDismiss = { showDialog = false })
    }

    val iconsList = listOf(
        Icons.Outlined.Palette,
        Icons.Outlined.Photo,
        Icons.Outlined.Brush,
        Icons.Outlined.CameraAlt,
    )

    // Changing the icon
    LaunchedEffect(Unit) {
        val delayTimeMillis = 1750L  // 1.75 seconds
        while (true) {
            delay(delayTimeMillis)
            currentIconIndex = (currentIconIndex + 1) % iconsList.size
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
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


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp, 0.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Hello,",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
                    .padding(0.dp, 0.dp, 0.dp, 0.dp),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Welcome to Museum App!",
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 8.dp, 0.dp),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                )
                Icon(
                    modifier = Modifier
                        .size(26.dp)
                        .align(Alignment.CenterVertically),
                    imageVector = iconsList[currentIconIndex],
                    contentDescription = "Decorative Image",
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }


            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(300.dp))
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp
                    ),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .padding(8.dp)
                        .size(width = 240.dp, height = 60.dp)
                        .padding(4.dp)
                        .clickable {
                            selectedCard("Start")
                            // Call the function to load Photograph Museum data
                            viewModel.fetchBeforePhotograhs()
                            navController.navigate("collectionList")
                        },

                    )
                {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Bắt đầu",
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                        )
                    }

                }
            }

        }
    }
}