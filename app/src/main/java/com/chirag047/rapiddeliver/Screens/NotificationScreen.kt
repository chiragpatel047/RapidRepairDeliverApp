package com.chirag047.rapiddeliver.Screens


import android.content.SharedPreferences
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chirag047.rapiddeliver.Common.ResponseType
import com.chirag047.rapiddeliver.Components.ActionBarWIthBack
import com.chirag047.rapiddeliver.Components.SingleNotification
import com.chirag047.rapiddeliver.Model.NotificationModel
import com.chirag047.rapiddeliver.Viewmodels.NotificationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotificationScreen(navController: NavController, sharedPreferences: SharedPreferences) {

    Box(Modifier.fillMaxSize()) {

        val scope = rememberCoroutineScope()
        val notificationViewModel: NotificationViewModel = hiltViewModel()

        val notificationList = remember {
            mutableListOf<NotificationModel>()
        }

        val result = notificationViewModel.requestsData.collectAsState()

        Column(Modifier.fillMaxWidth()) {
            ActionBarWIthBack(title = "Notification")

            LaunchedEffect(key1 = Unit) {
                scope.launch(Dispatchers.Main) {
                    notificationViewModel.getMyAllNotifications()
                }
            }

            when (result.value) {
                is ResponseType.Error -> {

                }

                is ResponseType.Loading -> {

                }

                is ResponseType.Success -> {
                    notificationList.clear()
                    notificationList.addAll(result.value.data!!)

                    LazyColumn() {
                        items(notificationList) {
                            SingleNotification(
                                notificationModel = it,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}