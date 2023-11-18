package com.example.vnmh.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.vnmh.data.remote.dto.MuseumItem

@Composable
fun CollectionDetailView(selectedItem: MuseumItem) {

    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .data(data = selectedItem.images).apply(block = fun ImageRequest.Builder.() {
                crossfade(true)
            }).build()
    )

    val addImagePainter1 = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .data(data = selectedItem.image1) // Replace with the correct property holding the additional image URL
            .apply {
                crossfade(true)
            }
            .build()
    )

    val addImagePainter2 = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .data(data = selectedItem.image2) // Replace with the correct property holding the additional image URL
            .apply {
                crossfade(true)
            }
            .build()
    )

    val addImagePainter3 = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .data(data = selectedItem.image3) // Replace with the correct property holding the additional image URL
            .apply {
                crossfade(true)
            }
            .build()
    )


    Box(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(24.dp, 24.dp),
                alignment = Alignment.Center,
            )
            Text(
                text = "${selectedItem.title}, ${selectedItem.year}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 24.dp, top = 8.dp, end = 24.dp, bottom = 8.dp)
            )
            Text(text = selectedItem.nonPresenterAuthorsName.trim().takeIf { it.isNotEmpty() }
                ?: "Unknown",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(start = 24.dp, top = 0.dp, end = 24.dp, bottom = 24.dp)
            )

            Text(
                text = selectedItem.imageDescription,
                modifier = Modifier
                    .padding(start = 24.dp, top = 0.dp, end = 24.dp, bottom = 24.dp)
            )
            addImageWithDescription(selectedItem.image1, selectedItem.description1)
            addImageWithDescription(selectedItem.image2, selectedItem.description2)
            addImageWithDescription(selectedItem.image3, selectedItem.description3)
        }
    }
}

@Composable
fun addImageWithDescription(imageUrl: String, description: String) {
    if (imageUrl.isNotEmpty()) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(data = imageUrl)
                    .apply {
                        crossfade(true)
                    }
                    .build()
            ),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f) // Set the aspect ratio to 1:1
                .padding(0.dp, 0.dp),
            alignment = Alignment.Center,
        )

        // Check if the description is not empty
        if (description.isNotEmpty()) {
            Text(
                text = description,
                modifier = Modifier
                    .padding(start = 5.dp, top = 0.dp, end = 0.dp, bottom = 0.dp)
            )
        }
    }
}