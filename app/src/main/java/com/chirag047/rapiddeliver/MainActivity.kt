package com.chirag047.rapiddeliver

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chirag047.rapiddeliver.Screens.ClientIssueDetailScreen
import com.chirag047.rapiddeliver.Screens.ClientLocationScreen
import com.chirag047.rapiddeliver.Screens.ForgetPassword
import com.chirag047.rapiddeliver.Screens.LoginScreen
import com.chirag047.rapiddeliver.Screens.MainScreen
import com.chirag047.rapiddeliver.Screens.SelectCityScreen
import com.chirag047.rapiddeliver.Screens.SignUpScreen
import com.chirag047.rapiddeliver.Screens.WelcomeScreen
import com.chirag047.rapiddeliver.ui.theme.RapidDeliverTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RapidDeliverTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
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
                MainScreen(navController, sharedPreferences)
            }
            composable(route = "SelectCityScreen") {
                SelectCityScreen(navController, sharedPreferences)
            }

            composable(route = "ClientIssueDetailScreen" + "/{orderId}/{userId}/{corporateId}/{corporateName}/{corporateAddress}/{vehicleOwner}/{vehicleType}/{vehicleCompany}/{vehicleModel}/{vehicleFuelType}/{vehicleLicensePlate}/{serviceType}/{clientAddress}/{clientLatitude}/{clientLongitude}/{clientAddedText}") {

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

                ClientIssueDetailScreen(
                    navController,
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
                    clientAddedText
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

}
