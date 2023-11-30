package com.example.vnmh.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
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

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {
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
            Text(
                text = "Toàn cảnh Bảo tàng",
                fontSize = 20.sp,
                modifier = Modifier.padding(20.dp, 24.dp, 20.dp, 2.dp),
                fontWeight = FontWeight.SemiBold
            )

            // Card for "Bảo tàng lịch sử"

            arItems.filter { it.name == "Bảo tàng lịch sử" }.forEach { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp, 6.dp)
                        .clickable {
                            val encodedItemId = URLEncoder.encode(item.id, "UTF-8")
                            navController.navigate("arView/${encodedItemId}")
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    )
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
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(shape = RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                    )
                }
            }

           Row (
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(20.dp, 20.dp, 20.dp, 2.dp),
               horizontalArrangement = Arrangement.SpaceBetween,
               verticalAlignment = Alignment.CenterVertically
           ){
                Text(
                    text = "Một số hiện vật",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = Icons.Filled.ArrowForwardIos,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }


            Spacer(modifier = Modifier.height(12.dp))

            // LazyRow for other items
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
            ) {
                items(arItems.filter { it.name != "Bảo tàng lịch sử" }) { item ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp, 6.dp)
                            .size(160.dp, 240.dp)
                            .clickable {
                                val encodedItemId = URLEncoder.encode(item.id, "UTF-8")
                                navController.navigate("arView/${encodedItemId}")
                            }
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
                                .size(160.dp)
                                .clip(shape = RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = item.name,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .widthIn(max = 180.dp)
                                .padding(horizontal = 12.dp),
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}
