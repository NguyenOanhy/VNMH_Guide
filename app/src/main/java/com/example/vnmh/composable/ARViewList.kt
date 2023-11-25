package com.example.vnmh.composable

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.vnmh.data.remote.dto.ARItem
import com.example.vnmh.util.isInternetAvailable
import com.example.vnmh.viewModel.MuseumViewModel
import java.net.URLEncoder

@Composable
fun ARViewList(
    arItems: List<ARItem>,
    viewModel: MuseumViewModel,
    navController: NavController,
) {
    val context = LocalContext.current
    val isConnected = isInternetAvailable(context)


    Column {
        Text(
            text = "VR 3D",
            modifier = Modifier.padding(20.dp, 24.dp, 20.dp, 2.dp),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        viewModel.fetchUrlAR()
        Spacer(modifier = Modifier.width(12.dp))

        if (!isConnected) {
            NoInternetPlaceholder()
        } else {
            Spacer(modifier = Modifier.height(6.dp))
            LazyColumn {
                items(arItems) { item ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp, 6.dp)
                            .clickable {
                                Log.d("MyApp", "Navigating to ARView/${item.id}")
                                // encode item.id to use the value in the navigation route
                                val encodedItemId = URLEncoder.encode(item.id, "UTF-8")
                                navController.navigate("arView/${encodedItemId}")
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                        ) {
                            val painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(LocalContext.current)
                                    .data(data = item.image)
                                    .apply(block = fun ImageRequest.Builder.() {
                                        crossfade(true)
                                    }).build()
                            )
                            Image(
                                painter = painter,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(124.dp)
                                    .clip(shape = RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop,
                                alignment = Alignment.Center,
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(
                                modifier = Modifier.weight(0.7f),
                            ) {
                                Text(
                                    text = item.name,
                                    fontSize = 16.sp,
                                    modifier = Modifier
                                        .widthIn(max = 200.dp),
                                    color = Color.Black,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))

                        }
                    }
                }
            }
        }
    }



}
