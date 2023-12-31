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
    object Home: NavigationItem(route = "collectionsCard", "Trang chủ", Icons.Filled.Collections, Icons.Outlined.Collections)
    object Collection: NavigationItem(route = "collectionList", "Bộ sưu tập", Icons.Filled.Collections, Icons.Outlined.Collections)
    object ARView: NavigationItem(route = "arviewList", "ARView", Icons.Filled.ViewInAr, Icons.Outlined.ViewInAr)

    object Camera: NavigationItem(route = "camera", "Quét mã", Icons.Filled.QrCodeScanner, Icons.Outlined.QrCodeScanner)
    object Favourite: NavigationItem(route = "favourite", "Mục ưa thích", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder)

}
