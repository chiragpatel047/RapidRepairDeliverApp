package com.chirag047.rapiddeliver.Screens

import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chirag047.rapiddeliver.Common.NoDataText
import com.chirag047.rapiddeliver.Common.ResponseType
import com.chirag047.rapiddeliver.Components.poppinsBoldCenterText
import com.chirag047.rapiddeliver.Components.poppinsBoldText
import com.chirag047.rapiddeliver.Components.poppinsText
import com.chirag047.rapiddeliver.Model.OrderModel
import com.chirag047.rapiddeliver.R
import com.chirag047.rapiddeliver.Viewmodels.HistoryScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HistoryScreen(navController: NavController, sharedPreferences: SharedPreferences) {
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxWidth()) {
            val doneOrdersList = remember {
                mutableListOf<OrderModel>()
            }

            val scope = rememberCoroutineScope()
            val historyScreenViewModel: HistoryScreenViewModel = hiltViewModel()
            val historyRequestState = historyScreenViewModel.historyRequests.collectAsState()

            poppinsBoldCenterText(
                contentText = "History",
                size = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            )

            val scroll = rememberScrollState()

            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(scroll)
            ) {
                Spacer(modifier = Modifier.padding(4.dp))

                val mechanicId = sharedPreferences.getString("mechanicId", "")!!

                LaunchedEffect(key1 = Unit) {
                    CoroutineScope(Dispatchers.IO).launch {
                        historyScreenViewModel.getMyOrdersRequest(mechanicId)
                    }
                }

                when (historyRequestState.value) {
                    is ResponseType.Error -> {

                    }

                    is ResponseType.Loading -> {

                    }

                    is ResponseType.Success -> {
                        doneOrdersList.clear()
                        doneOrdersList.addAll(historyRequestState.value.data!!)

                    }
                }

                NoDataText("No history", doneOrdersList.size.equals(0))
                loadDoneRequests(doneOrdersList.reversed(), navController)

            }
        }
    }
}


@Composable
fun TrackHistorySingle(title: String, desc: String, orderInfo: String) {

    Column(
        Modifier
            .padding(15.dp, 7.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable {

            },
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                Modifier
                    .padding(15.dp, 0.dp, 7.dp, 0.dp)
                    .clip(RoundedCornerShape(10.dp))

            ) {
                Spacer(modifier = Modifier.padding(10.dp))
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .padding(5.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(MaterialTheme.colorScheme.secondary),
                ) {
                    Icon(
                        painterResource(id = R.drawable.history),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(10.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
            }

            Column(Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.padding(10.dp))

                poppinsBoldText(
                    contentText = title,
                    size = 14.sp,
                    modifier = Modifier
                        .padding(10.dp, 0.dp)
                )

                poppinsText(
                    contentText = desc,
                    size = 12.sp,
                    modifier = Modifier
                        .padding(10.dp, 0.dp)
                )

                Spacer(modifier = Modifier.padding(10.dp))

            }

        }
        Text(
            text = orderInfo,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary,
            fontFamily = FontFamily(Font(R.font.poppins_medium)),
            modifier = Modifier
                .padding(20.dp, 0.dp, 20.dp, 20.dp)
        )
    }
}

@Composable
fun loadDoneRequests(list: List<OrderModel>, navController: NavController) {
    list.forEach {

        TrackHistorySingle(
            it.vehicleOwner,
            it.vehicleCompany + " " + it.vehicleModel + " | " + it.vehicleFuelType,
            it.orderInfo
        )
    }
}
