package com.chirag047.rapiddeliver.Screens


import android.content.SharedPreferences
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chirag047.rapiddeliver.Common.ResponseType
import com.chirag047.rapiddeliver.Components.FullWidthButton
import com.chirag047.rapiddeliver.Components.SnackbarWithoutScaffold
import com.chirag047.rapiddeliver.Components.customProgressBar
import com.chirag047.rapiddeliver.Components.poppinsBoldCenterText
import com.chirag047.rapiddeliver.Components.poppinsBoldText
import com.chirag047.rapiddeliver.Components.poppinsText
import com.chirag047.rapiddeliver.R
import com.chirag047.rapiddeliver.Viewmodels.SelectCityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SelectCityScreen(navController: NavController, sharedPreferences: SharedPreferences) {

    val scope = rememberCoroutineScope()

    val selectCityViewModel: SelectCityViewModel = hiltViewModel()

    val showProgressBar = remember {
        mutableStateOf(false)
    }

    var openMySnackbar = remember { mutableStateOf(false) }
    var snackBarMsg = remember { mutableStateOf("") }

    var selectedCity = remember {
        mutableStateOf("Select city")
    }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxWidth()) {

            Column(Modifier.fillMaxWidth()) {
                poppinsBoldCenterText(
                    contentText = "Please select city",
                    size = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                )
            }

            poppinsBoldText(
                contentText = "Choose city",
                size = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 15.dp, 15.dp, 0.dp)
            )

            val citiesInIndia = listOf(
                "Ahmedabad",
                "Bangalore",
                "Bhopal",
                "Chennai",
                "Delhi",
                "Ghaziabad",
                "Hyderabad",
                "Indore",
                "Jaipur",
                "Kanpur",
                "Kolkata",
                "Lucknow",
                "Mumbai",
                "Nagpur",
                "Patna",
                "Pune",
                "Surat",
                "Thane",
                "Vadodara",
                "Visakhapatnam"
            )

            val showDialog = remember { mutableStateOf(false) }

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(50.dp))
                .clickable {
                    showDialog.value = !showDialog.value
                }) {
                Text(
                    text = selectedCity.value,
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                    modifier = Modifier.padding(15.dp)
                )
            }

            if (showDialog.value)
                CustomDialog(list = citiesInIndia, setShowDialog = { isShow, selected ->
                    showDialog.value = isShow
                    selectedCity.value = selected
                    Log.d("CusomeDialogCustomeValue", selected)
                })
        }

        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(0.dp, 0.dp, 0.dp, 30.dp)
        ) {
            FullWidthButton(label = "Next", color = MaterialTheme.colorScheme.primary) {
                if (selectedCity.value.equals("Select city")) {
                    snackBarMsg.value = "Please select city"
                    openMySnackbar.value = true
                    return@FullWidthButton
                }

                scope.launch(Dispatchers.Main) {
                    selectCityViewModel.updateUserCity(
                        selectedCity.value,
                        sharedPreferences.getString("mechanicId", "")!!
                    ).collect {
                        when (it) {
                            is ResponseType.Error -> {
                                snackBarMsg.value = "Something went wrong"
                                openMySnackbar.value = true
                            }

                            is ResponseType.Loading -> {
                                showProgressBar.value = true
                            }

                            is ResponseType.Success -> {

                                showProgressBar.value = false

                                navController.popBackStack()
                                navController.popBackStack()

                                sharedPreferences.edit().putBoolean("isFilled", true).apply()
                                sharedPreferences.edit().putString("userCity", selectedCity.value)
                                    .apply()

                                navController.navigate("MainScreen")

                            }
                        }
                    }
                }
            }
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
fun CustomDialog(
    list: List<String>,
    setShowDialog: (Boolean, String) -> Unit
) {

    var selected = "Select city"

    Dialog(onDismissRequest = {
        setShowDialog(false, selected)
    }) {
        Surface {
            val scroll = rememberScrollState()
            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(scroll)
            ) {
                list.forEach {
                    poppinsText(
                        contentText = it,
                        size = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                            .clickable {
                                selected = it
                                setShowDialog(false, selected)
                            }
                    )
                }
            }
        }
    }
}