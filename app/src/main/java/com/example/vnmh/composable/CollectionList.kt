package com.example.vnmh.composable

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.vnmh.data.remote.dto.MuseumItem
import com.example.vnmh.room.FavouriteItem
import com.example.vnmh.ui.theme.ColorProvider
import com.example.vnmh.util.isInternetAvailable
import com.example.vnmh.viewModel.FavouriteViewModel
import com.example.vnmh.viewModel.MuseumViewModel
import java.net.URLEncoder

@Composable
fun CollectionList(
    museumItems: List<MuseumItem>,
    viewModel: MuseumViewModel,
    selectedCard: String,
    navController: NavController,
    favouriteViewModel: FavouriteViewModel,
    selectedTabIndex: Int
) {
    // Check for internet availability
    val context = LocalContext.current
    val isConnected = isInternetAvailable(context)
    var selectedTabIndex by remember { mutableIntStateOf(selectedTabIndex) }

    val tabs = when (selectedCard) {
        "Start" -> listOf("Trước năm 1945", "Sau năm 1945")
        else -> emptyList()
    }

    val scrollStateTab1 = remember { LazyListState() }
    val scrollStateTab2 = remember { LazyListState() }

    Column {
        // Content based on the selected tab
        when (selectedCard) {
            "Start" -> {
                when (selectedTabIndex) {
                    0 -> {
                        Text(
                            text = "Bảo tàng Lịch sử Quốc gia",
                            modifier = Modifier
                                .padding(20.dp, 24.dp, 20.dp, 20.dp)
                                .align(Alignment.CenterHorizontally),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        viewModel.fetchBeforePhotograhs()
                    }
                    1 -> {
                        Text(
                            text = "Bảo tàng Lịch sử Quốc gia",
                            modifier = Modifier
                                .padding(20.dp, 24.dp, 20.dp, 20.dp)
                                .align(Alignment.CenterHorizontally),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        viewModel.fetchAfterPhotographs()
                    }
                }
            }
        }

        // Tab Row
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            modifier = Modifier.padding(12.dp, 0.dp),
            divider = {},
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    color = ColorProvider.mainColor,
                    height = 2.dp,
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                    },
                    text = {
                        Text(
                            text = title,
                            fontSize = 16.sp,
                            fontWeight = if(selectedTabIndex==index) FontWeight.Bold else FontWeight.Normal,
                            color = if(selectedTabIndex==index) ColorProvider.mainColor else Color.Black
                        )
                    },
                    selectedContentColor = ColorProvider.mainColor,
                )
            }
        }



        // Observe changes in the list of favorite items from the ViewModel
        val favouriteItems by favouriteViewModel.favouriteItems.observeAsState(emptyList())

        // Initialize a map to store the isFavourite state for each item
        val isFavouriteMap = remember { mutableStateMapOf<String, Boolean>() }

        // Update the isFavouriteMap whenever favouriteItems changes
        LaunchedEffect(favouriteItems) {
            favouriteItems.forEach { favouriteItem ->
                isFavouriteMap[favouriteItem.id] = favouriteItem.isFavourite
            }
        }

        if (!isConnected) {
            NoInternetPlaceholder()
        } else {
            Spacer(modifier = Modifier.height(6.dp))

            LazyColumn(state = when (selectedTabIndex) {
                0 -> scrollStateTab1
                1 -> scrollStateTab2
                else -> rememberLazyListState()
            }) {
                items(museumItems) { item ->

                    val isFavouriteState = isFavouriteMap[item.id] ?: false

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp, 6.dp)
                            .clickable {
                                Log.d("MyApp", "Navigating to collectionDetailView/${item.id}")
                                // encode item.id to use the value in the navigation route
                                val encodedItemId = URLEncoder.encode(item.id, "UTF-8")
                                navController.navigate("collectionDetailView/${encodedItemId}")
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
                                    .data(data = item.images)
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
                                    text = item.title,
                                    fontSize = 16.sp,
                                    modifier = Modifier
                                        .widthIn(max = 200.dp),
                                    color = Color.Black,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.nonPresenterAuthorsName.trim()
                                        .takeIf { it.isNotEmpty() } ?: "Unknown",
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Box {
                                IconButton(
                                    onClick = {
                                        val newFavouriteState = !isFavouriteState
                                        isFavouriteMap[item.id] = newFavouriteState

                                        val favouriteItem = FavouriteItem(
                                            id = item.id,
                                            images = item.images,
                                            imageDescription = item.imageDescription,
                                            nonPresenterAuthorsName = item.nonPresenterAuthorsName,
                                            title = item.title,
                                            year = item.year,
                                            image1 = item.image1,
                                            description1 = item.description1,
                                            image2 = item.image2,
                                            description2 = item.description2,
                                            image3 = item.image3,
                                            description3 = item.description3,
                                            isFavourite = newFavouriteState
                                        )

                                        if (newFavouriteState) {
                                            favouriteViewModel.saveFavoriteItem(favouriteItem)
                                            Log.d(
                                                "DBG",
                                                "Marked item ${favouriteItem.id} as a favourite"
                                            )
                                        } else {
                                            favouriteViewModel.deleteFavoriteItem(favouriteItem)
                                            Log.d(
                                                "DBG",
                                                "Removed item ${favouriteItem.id} from favourites"
                                            )
                                        }

                                        favouriteViewModel.updateFavoriteItem(favouriteItem)

                                    },
                                    modifier = Modifier
                                        .size(30.dp)
                                        .align(Alignment.TopEnd)
                                ) {
                                    val icon =
                                        if (isFavouriteState) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = "Mục ưa thích",
                                        modifier = Modifier.size(24.dp),
                                        tint = ColorProvider.mainColor
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}