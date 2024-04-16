package com.chirag047.rapiddeliver.Screens


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.chirag047.rapiddeliver.Components.poppinsBoldCenterText
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay

@Composable
fun ClientLocationScreen(
    navController: NavController, clientLatitude: String,
    clientLongitude: String

) {
    Box(Modifier.fillMaxSize()) {

        val loadMap = remember {
            mutableStateOf(false)
        }

        LaunchedEffect(key1 = Unit) {
            delay(500)
            loadMap.value = true
        }

        Column(Modifier.fillMaxWidth()) {
            Column(Modifier.fillMaxWidth()) {
                poppinsBoldCenterText(
                    contentText = "Client location",
                    size = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                )
            }

            val latitude = clientLatitude.toDouble()
            val longitude = clientLongitude.toDouble()

            loadClientLocationOnMap(load = loadMap.value, marker = LatLng(latitude, longitude))

        }
    }
}

@Composable
fun loadClientLocationOnMap(load: Boolean, marker: LatLng) {
    AnimatedVisibility(visible = load) {

        val markerState =
            remember { mutableStateOf(MarkerState(position = marker)) }

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(markerState.value.position, 12f)
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(state = markerState.value)
        }
    }
}