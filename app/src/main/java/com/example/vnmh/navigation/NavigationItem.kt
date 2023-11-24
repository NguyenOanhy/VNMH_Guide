package com.example.vnmh.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.ViewInAr
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem (
    val route: String,
    val label:String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Home1: NavigationItem(route = "collectionsCard", "Home", Icons.Filled.Collections, Icons.Outlined.Collections)
    object Home: NavigationItem(route = "collectionList", "Collection", Icons.Filled.Collections, Icons.Outlined.Collections)
    object ARView: NavigationItem(route = "arview", "ARView", Icons.Filled.ViewInAr, Icons.Outlined.ViewInAr)

    object Camera: NavigationItem(route = "camera", "Scanner", Icons.Filled.QrCodeScanner, Icons.Outlined.QrCodeScanner)
    object Favourite: NavigationItem(route = "favourite", "Favorites", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder)

}
