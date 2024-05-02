package com.chirag047.rapiddeliver

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chirag047.rapiddeliver.Screens.ClientIssueDetailScreen
import com.chirag047.rapiddeliver.Screens.ClientLocationScreen
import com.chirag047.rapiddeliver.Screens.EditProfile
import com.chirag047.rapiddeliver.Screens.ForgetPassword
import com.chirag047.rapiddeliver.Screens.LoginScreen
import com.chirag047.rapiddeliver.Screens.MainScreen
import com.chirag047.rapiddeliver.Screens.NotificationScreen
import com.chirag047.rapiddeliver.Screens.SelectCityScreen
import com.chirag047.rapiddeliver.Screens.SignUpScreen
import com.chirag047.rapiddeliver.Screens.TrackNowScreen
import com.chirag047.rapiddeliver.Screens.WelcomeScreen
import com.chirag047.rapiddeliver.ui.theme.RapidDeliverTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val backgroundLocationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {

        }

    val notificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {

            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val settingsIntent: Intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                    startActivity(settingsIntent)
                }
            }
        }

    val locationPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            when {
                it.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if (ActivityCompat.checkSelfPermission(
                                this,
                                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            backgroundLocationPermission.launch(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        }
                    }
                }

                it.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {

                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RapidDeliverTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    checkPermission()

                    val auth = Firebase.auth
                    val sharedPreferences =
                        getSharedPreferences("isDataFilledPrefrence", Context.MODE_PRIVATE)

                    val isFilled = sharedPreferences.getBoolean("isFilled", false)

                    if (auth.currentUser != null) {
                        if (isFilled) {
                            App("MainScreen", sharedPreferences)
                        } else {
                            App("SelectCityScreen", sharedPreferences)
                        }
                    } else {
                        App("WelcomeScreen", sharedPreferences)
                    }
                }
            }
        }
    }

    @Composable
    fun App(startScreen: String, sharedPreferences: SharedPreferences) {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = startScreen) {
            composable(route = "WelcomeScreen") {
                WelcomeScreen(navController)
            }
            composable(route = "SignUpScreen") {
                SignUpScreen(navController, sharedPreferences)
            }
            composable(route = "LoginScreen") {
                LoginScreen(navController, sharedPreferences)
            }
            composable(route = "ForgetPassword") {
                ForgetPassword(navController)
            }

            composable(route = "MainScreen") {
                MainScreen(navController, sharedPreferences,this@MainActivity)
            }
            composable(route = "SelectCityScreen") {
                SelectCityScreen(navController, sharedPreferences)
            }
            composable(route = "EditProfile") {
                EditProfile(navController, sharedPreferences)
            }

            composable(route = "NotificationScreen") {
                NotificationScreen(navController, sharedPreferences)
            }

            composable(route = "TrackNowScreen" + "/{orderId}/{clientAddress}/{clientLatitude}/{clientLongitude}/{centerId}/{userId}/{vehicleOwner}") {

                val orderId = it.arguments?.getString("orderId")!!
                val clientAddress = it.arguments?.getString("clientAddress")!!
                val clientLatitude = it.arguments?.getString("clientLatitude")!!
                val clientLongitude = it.arguments?.getString("clientLongitude")!!
                val centerId = it.arguments?.getString("centerId")!!
                val userId = it.arguments?.getString("userId")!!
                val vehicleOwner = it.arguments?.getString("vehicleOwner")!!

                TrackNowScreen(
                    navController, orderId, clientAddress,
                    clientLatitude,
                    clientLongitude,
                    centerId,
                    userId,
                    vehicleOwner,
                    this@MainActivity
                )
            }

            composable(route = "ClientIssueDetailScreen" + "/{orderId}/{userId}/{corporateId}/{corporateName}/{corporateAddress}/{vehicleOwner}/{vehicleType}/{vehicleCompany}/{vehicleModel}/{vehicleFuelType}/{vehicleLicensePlate}/{serviceType}/{clientAddress}/{clientLatitude}/{clientLongitude}/{clientAddedText}/{mechanicStatus}") {

                val orderId = it.arguments?.getString("orderId")!!
                val userId = it.arguments?.getString("userId")!!
                val corporateId = it.arguments?.getString("corporateId")!!
                val corporateName = it.arguments?.getString("corporateName")!!
                val corporateAddress = it.arguments?.getString("corporateAddress")!!
                val serviceType = it.arguments?.getString("serviceType")!!
                val vehicleOwner = it.arguments?.getString("vehicleOwner")!!
                val vehicleType = it.arguments?.getString("vehicleType")!!
                val vehicleCompany = it.arguments?.getString("vehicleCompany")!!
                val vehicleModel = it.arguments?.getString("vehicleModel")!!
                val vehicleFuelType = it.arguments?.getString("vehicleFuelType")!!
                val vehicleLicensePlate = it.arguments?.getString("vehicleLicensePlate")!!
                val clientAddress = it.arguments?.getString("clientAddress")!!
                val clientLatitude = it.arguments?.getString("clientLatitude")!!
                val clientLongitude = it.arguments?.getString("clientLongitude")!!
                val clientAddedText = it.arguments?.getString("clientAddedText")!!
                val mechanicStatus = it.arguments?.getString("mechanicStatus")!!

                ClientIssueDetailScreen(
                    navController,
                    sharedPreferences,
                    this@MainActivity,
                    orderId,
                    userId,
                    corporateId,
                    corporateName,
                    corporateAddress,
                    serviceType,
                    vehicleOwner,
                    vehicleType,
                    vehicleCompany,
                    vehicleModel,
                    vehicleFuelType,
                    vehicleLicensePlate,
                    clientAddress,
                    clientLatitude,
                    clientLongitude,
                    clientAddedText,
                    mechanicStatus
                )
            }

            composable(route = "ClientLocationScreen" + "/{clientLatitude}/{clientLongitude}") {

                val clientLatitude = it.arguments?.getString("clientLatitude")!!
                val clientLongitude = it.arguments?.getString("clientLongitude")!!

                ClientLocationScreen(
                    navController, clientLatitude,
                    clientLongitude
                )
            }
        }
    }

    @Composable
    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                locationPermissions.launch(
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            } else {
                backgroundLocationPermission.launch(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)

            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                backgroundLocationPermission.launch(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermission.launch(
                    android.Manifest.permission.POST_NOTIFICATIONS
                )
            }
        }

    }

}
