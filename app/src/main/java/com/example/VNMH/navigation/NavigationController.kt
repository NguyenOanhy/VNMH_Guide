package com.example.VNMH.navigation

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.VNMH.composable.CameraView
import com.example.VNMH.composable.CollectionDetailView
import com.example.VNMH.composable.CollectionList
import com.example.VNMH.composable.CollectionsCard
import com.example.VNMH.composable.FavouritesView
import com.example.VNMH.viewModel.FavouriteViewModel
import com.example.VNMH.viewModel.MuseumViewModel
import com.google.firebase.annotations.PreviewApi
import io.ktor.http.ContentType

@Composable
fun NavigationController(
    navController: NavHostController,
    viewModel: MuseumViewModel,
    favouriteViewModel: FavouriteViewModel
) {
    // Function to get the image URL from Firebase Storage
    fun getImageUrl(): String {
        return "https://firebasestorage.googleapis.com/v0/b/ticket-b992f.appspot.com/o/images%2FBaoTangLichSu.jpg?alt=media&token=d3ffd783-1148-43eb-a969-db61c264b5bc"
    }

    // State to track the selected card
    val selectedCard = remember { mutableStateOf("") }

    // Get the current route
    val currentRoute = currentRoute(navController)

    // Use a Box to layer the background image behind the NavHost
    Box(modifier = Modifier.fillMaxSize()) {
        // Background image using Coil's rememberImagePainter
        if (currentRoute == NavigationItem.Home.route) {
            Image(
                painter = rememberImagePainter(getImageUrl()),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // NavHost containing your composables
        NavHost(navController = navController, startDestination = NavigationItem.Home.route) {
            composable(NavigationItem.Home.route) {
                CollectionsCard(navController, viewModel) { cardType ->
                    selectedCard.value = cardType
                }
            }

            composable("collectionList") {
                val museumData by viewModel.museumData.observeAsState(emptyList())
                CollectionList(
                    museumData,
                    viewModel,
                    selectedCard.value,
                    navController,
                    favouriteViewModel
                )
            }

            composable("collectionDetailView/{itemId}") { backStackEntry ->
                // Extract the item ID from the navigation arguments
                val itemId = backStackEntry.arguments?.getString("itemId")
                val museumData by viewModel.museumData.observeAsState(emptyList())
                // Retrieve the corresponding MuseumItem based on the item ID
                val selectedItem = museumData.find { it.id == itemId }

                if (selectedItem != null) {
                    // Pass the selected item to the CollectionDetailView composable
                    CollectionDetailView(selectedItem)
                } else {
                    // Handle the case where the item is not found
                    Text(text = "Item not found", fontSize = 20.sp)
                }
            }

            composable(NavigationItem.Camera.route) {
                CameraView()
            }

            composable(NavigationItem.Favourite.route) {
                FavouritesView(favouriteViewModel)
            }
        }
    }

}


// Function to get the current route
@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}


@Composable
fun Material3BottomBar(
    navController: NavHostController,
    items: List<NavigationItem>
) {
    NavigationBar(
        modifier = Modifier,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            val isSelected = when (item) {
                NavigationItem.Home -> currentRoute == item.route || currentRoute == "collectionList" || currentRoute == "collectionDetailView/{itemId}"
                else -> currentRoute == item.route
            }

            NavigationBarItem(
                label = {
                    Text(text = item.label)
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.surfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                    selectedTextColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Composable
fun Navigation(viewModel: MuseumViewModel, favouriteViewModel: FavouriteViewModel) {
    val navController = rememberNavController()
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Camera,
        NavigationItem.Favourite
    )

    Scaffold(
        bottomBar = {
            Material3BottomBar(navController = navController, items = items)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NavigationController(
                navController = navController,
                viewModel = viewModel,
                favouriteViewModel = favouriteViewModel
            )
        }
    }
}


@Composable
@Preview
fun NavigationControllerPreview() {
    val navViewModel = MuseumViewModel()
    val favouriteViewModel = FavouriteViewModel(application = Application())

    val navController = rememberNavController()
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Camera,
        NavigationItem.Favourite
    )

    Material3BottomBar(navController = navController, items = items)
}

