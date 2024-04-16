package com.chirag047.rapiddeliver.Common

import com.chirag047.rapiddeliver.R

sealed class NavigationItem(val route: String, val label: String, val icon: Int) {
    object HomeNav : NavigationItem("HomeScreen", "Home", R.drawable.house)
    object HistoryNav : NavigationItem("HistoryScreen", "History", R.drawable.history)

    object ProfileNav : NavigationItem("ProfileScreen", "Profile", R.drawable.account)

}