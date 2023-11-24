package com.example.vnmh.navigation

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vnmh.R
import com.example.vnmh.composable.CameraView
import com.example.vnmh.composable.ARView
import com.example.vnmh.composable.CollectionDetailView
import com.example.vnmh.composable.CollectionList
import com.example.vnmh.composable.CollectionsCard
import com.example.vnmh.composable.FavouritesView
import com.example.vnmh.viewModel.FavouriteViewModel
import com.example.vnmh.viewModel.MuseumViewModel

@Composable
fun NavigationController(
    navController: NavHostController,
    viewModel: MuseumViewModel,
    favouriteViewModel: FavouriteViewModel
) {

    // State to track the selected card
    val selectedCard = remember { mutableStateOf("") }

    // Get the current route
    val currentRoute = currentRoute(navController)

    // Use a Box to layer the background image behind the NavHost
    Box(modifier = Modifier.fillMaxSize()) {
        // Background image using Coil's rememberImagePainter
        val backgroundImage = painterResource(id = R.drawable.homebg)
        if (currentRoute == NavigationItem.Home1.route) {
            Image(
                painter = backgroundImage,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // NavHost containing your composables
        NavHost(navController = navController, startDestination = NavigationItem.Home1.route) {
            composable(NavigationItem.Home1.route) {
                CollectionsCard(navController = navController, viewModel = viewModel)

            }

            composable(NavigationItem.Home.route) {
                val museumData by viewModel.museumData.observeAsState(emptyList())
                selectedCard.value = "Start"
                viewModel.fetchBeforePhotograhs()
                CollectionList(
                    museumData,
                    viewModel,
                    selectedCard.value,
                    navController,
                    favouriteViewModel
                )
            }

//            composable("collectionList") {
//                val museumData by viewModel.museumData.observeAsState(emptyList())
//                CollectionList(
//                    museumData,
//                    viewModel,
//                    selectedCard.value,
//                    navController,
//                    favouriteViewModel
//                )
//            }

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

            composable(NavigationItem.ARView.route) {
                ARView()
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
        NavigationItem.ARView,
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

