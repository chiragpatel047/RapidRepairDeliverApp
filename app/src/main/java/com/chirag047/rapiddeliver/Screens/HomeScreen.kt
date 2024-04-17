package com.chirag047.rapiddeliver.Screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chirag047.rapiddeliver.Common.NoDataText
import com.chirag047.rapiddeliver.Common.ResponseType
import com.chirag047.rapiddeliver.Common.SingleSerivceRequest
import com.chirag047.rapiddeliver.Common.TrackSingle
import com.chirag047.rapiddeliver.Components.GrayFilledSimpleButton
import com.chirag047.rapiddeliver.Components.SnackbarWithoutScaffold
import com.chirag047.rapiddeliver.Components.customProgressBar
import com.chirag047.rapiddeliver.Components.poppinsBoldCenterText
import com.chirag047.rapiddeliver.Components.poppinsBoldText
import com.chirag047.rapiddeliver.Components.textWithSeeAllText
import com.chirag047.rapiddeliver.Model.OrderModel
import com.chirag047.rapiddeliver.R
import com.chirag047.rapiddeliver.Viewmodels.HomeScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController, sharedPreferences: SharedPreferences) {

    Box(Modifier.fillMaxSize()) {

        val homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
        val scope = rememberCoroutineScope()
        val scroll = rememberScrollState()

        val liveRequestState = homeScreenViewModel.liveRequests.collectAsState()
        val pendingRequestState = homeScreenViewModel.pendingRequests.collectAsState()

        val showProgressBar = remember {
            mutableStateOf(false)
        }

        var openMySnackbar = remember { mutableStateOf(false) }
        var snackBarMsg = remember { mutableStateOf("") }


        val mechanicCity = remember {
            mutableStateOf("...")
        }

        val mechanicId = remember {
            mutableStateOf("...")
        }

        val mechanicName = remember {
            mutableStateOf("...")
        }

        val mechanicStatus = remember {
            mutableStateOf("...")
        }

        val centerName = remember {
            mutableStateOf("...")
        }

        val pendingOrdersList = remember {
            mutableListOf<OrderModel>()
        }

        val liveOrdersList = remember {
            mutableListOf<OrderModel>()
        }

        LaunchedEffect(key1 = Unit) {
            scope.launch(Dispatchers.Main) {
                homeScreenViewModel.getUserDetail().collect {
                    when (it) {
                        is ResponseType.Error -> {

                        }

                        is ResponseType.Loading -> {

                        }

                        is ResponseType.Success -> {
                            mechanicCity.value = it.data!!.city
                            mechanicId.value = it.data!!.mechanicId
                            mechanicName.value = it.data!!.userName
                            mechanicStatus.value = it.data!!.mechanicStatus
                            centerName.value = it.data.centerName
                        }
                    }
                }
            }
        }

        LaunchedEffect(key1 = Unit) {
            CoroutineScope(Dispatchers.IO).launch {
                val mechanicId = sharedPreferences.getString("mechanicId", "")!!
                homeScreenViewModel.getLiveRequest(mechanicId)
                Log.d("liveOrderListLogDataRequest", "liveOrderListLogDataRequest")

            }
        }

        when (liveRequestState.value) {
            is ResponseType.Error -> {

            }

            is ResponseType.Loading -> {

            }

            is ResponseType.Success -> {
                liveOrdersList.clear()
                liveOrdersList.addAll(liveRequestState.value.data!!)
            }
        }


        LaunchedEffect(key1 = Unit) {
            CoroutineScope(Dispatchers.Main).launch {
                val mechanicId = sharedPreferences.getString("mechanicId", "")!!
                homeScreenViewModel.getPendingRequest(mechanicId)
            }
        }

        when (pendingRequestState.value) {
            is ResponseType.Error -> {

            }

            is ResponseType.Loading -> {

            }

            is ResponseType.Success -> {

                pendingOrdersList.clear()
                pendingOrdersList.addAll(pendingRequestState.value.data!!)

            }
        }

        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(scroll)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {
                    Text(
                        text = "Location",
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                        fontSize = 12.sp
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Icon(
                            painter = painterResource(id = R.drawable.location_pin_icon),
                            contentDescription = "",
                            modifier = Modifier.size(15.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.padding(2.dp))
                        Text(
                            text = mechanicCity.value +
                                    " City, India",
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.poppins_medium)),
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.padding(2.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.down_arrow_icon),
                            contentDescription = "",
                            modifier = Modifier.size(12.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                GrayFilledSimpleButton(imageIcon = R.drawable.notification) {
                    navController.navigate("NotificationScreen")
                }
            }

            Box(
                Modifier
                    .padding(15.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {

                Column(Modifier.fillMaxWidth()) {
                    poppinsBoldCenterText(
                        contentText = mechanicName.value,
                        size = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp, 15.dp, 15.dp, 5.dp)
                    )

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(15.dp, 15.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.background),
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Column(
                            Modifier
                                .weight(1f)
                                .padding(10.dp, 15.dp, 15.dp, 0.dp)
                        ) {
                            Row(
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.service_center),
                                    contentDescription = "",
                                    Modifier
                                        .size(30.dp)
                                        .padding(0.dp, 8.dp, 0.dp, 8.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )

                                Text(
                                    text = centerName.value,
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .padding(0.dp, 6.dp, 0.dp, 5.dp),
                                    fontFamily = FontFamily(Font(R.font.poppins_medium))
                                )
                            }

                            Row() {
                                Icon(
                                    painter = painterResource(id = R.drawable.id_icon),
                                    contentDescription = "",
                                    Modifier
                                        .size(30.dp)
                                        .padding(0.dp, 8.dp, 0.dp, 8.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = mechanicId.value,
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .padding(0.dp, 6.dp, 0.dp, 5.dp),
                                    fontFamily = FontFamily(Font(R.font.poppins_medium))
                                )
                            }

                            Spacer(modifier = Modifier.padding(8.dp))

                        }

                        Button(
                            onClick = {

                            },
                            colors = ButtonDefaults.buttonColors(if (mechanicStatus.value.equals("Available")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary),
                            modifier = Modifier
                                .padding(0.dp, 0.dp, 10.dp, 0.dp)
                        ) {
                            Text(
                                text = mechanicStatus.value,
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.padding(4.dp))
            poppinsBoldText(
                contentText = "Current live request",
                size = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 5.dp, 15.dp, 0.dp)
            )

            Spacer(modifier = Modifier.padding(2.dp))

            NoDataText("No Live request", liveOrdersList.size.equals(0))
            loadLiveRequests(liveOrdersList, navController)

            Spacer(modifier = Modifier.padding(6.dp))

            textWithSeeAllText(title = "Pending requests") {

            }


            NoDataText("No pending request", pendingOrdersList.size.equals(0))

            loadPendingRequests(
                list = pendingOrdersList,
                navController = navController,
                homeScreenViewModel,
                mechanicStatus.value
            )

        }
        customProgressBar(show = showProgressBar.value, title = "Wait a moment...")

        SnackbarWithoutScaffold(
            snackBarMsg.value, openMySnackbar.value, { openMySnackbar.value = it }, Modifier.align(
                Alignment.BottomCenter
            )
        )
    }
}

@Composable
fun loadPendingRequests(
    list: List<OrderModel>,
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel,
    mechanicStatus: String
) {
    val scope = rememberCoroutineScope()

    list.forEach {
        SingleSerivceRequest(
            it,
            navController,
            mechanicStatus
        ) {
            if (mechanicStatus.equals("Available")) {


                scope.launch(Dispatchers.Main) {
                    homeScreenViewModel.startMechanicService(it.orderId).collect {
                        when (it) {
                            is ResponseType.Error -> {

                            }

                            is ResponseType.Loading -> {

                            }

                            is ResponseType.Success -> {

                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun loadLiveRequests(list: List<OrderModel>, navController: NavController) {
    list.forEach {
        TrackSingle(
            it.vehicleOwner,
            it.vehicleCompany + " " + it.vehicleModel + " | " + it.vehicleFuelType
        ) {
            navController.navigate("TrackNowScreen")
        }
    }
}