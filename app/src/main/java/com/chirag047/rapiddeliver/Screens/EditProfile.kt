package com.chirag047.rapiddeliver.Screens


import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.chirag047.rapiddeliver.Common.ResponseType
import com.chirag047.rapiddeliver.Components.ActionBarWIthBack
import com.chirag047.rapiddeliver.Components.FullWidthButton
import com.chirag047.rapiddeliver.Components.SnackbarWithoutScaffold
import com.chirag047.rapiddeliver.Components.customProgressBar
import com.chirag047.rapiddeliver.Components.poppinsBoldText
import com.chirag047.rapiddeliver.R
import com.chirag047.rapiddeliver.Viewmodels.ProfileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfile(navController: NavController, sharedPreferences: SharedPreferences) {

    val profileViewModel: ProfileViewModel = hiltViewModel()

    var imageUri = remember {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()

    val showProgressBar = remember {
        mutableStateOf(false)
    }

    var openMySnackbar = remember { mutableStateOf(false) }
    var snackBarMsg = remember { mutableStateOf("") }


    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri.value = uri.toString()
    }

    Box(Modifier.fillMaxSize()) {

        var nameText by remember { mutableStateOf("Jone snow") }
        var numberText by remember { mutableStateOf("") }
        var profileImage = remember {
            mutableStateOf("")
        }

        LaunchedEffect(key1 = Unit) {
            scope.launch(Dispatchers.IO) {
                profileViewModel.getUserDetails().collect {
                    when (it) {
                        is ResponseType.Error -> {

                        }

                        is ResponseType.Loading -> {

                        }

                        is ResponseType.Success -> {
                            nameText = it.data?.userName!!
                            numberText = it.data?.phoneNo!!
                            profileImage.value = it.data?.userImage!!
                        }
                    }
                }
            }
        }

        Column(Modifier.fillMaxWidth()) {
            ActionBarWIthBack(title = "Edit profile")

            Spacer(modifier = Modifier.padding(20.dp))

            Box(
                Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        launcher.launch("image/*")
                    }
            ) {

                if (imageUri.value.equals("")) {
                    if (profileImage.value.equals("")) {
                        Image(
                            painter = painterResource(R.drawable.profile_filled_icon),
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(Color.White),
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color.Gray)
                                .padding(10.dp)
                                .align(Alignment.TopCenter)
                        )
                    } else {
                        Image(
                            painter = rememberImagePainter(profileImage.value),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color.Black)
                                .align(Alignment.TopCenter)
                        )
                    }

                } else {
                    Image(
                        painter = rememberImagePainter(imageUri.value),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color.Black)
                            .align(Alignment.TopCenter)
                    )
                }


                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .align(Alignment.BottomEnd)
                        .background(MaterialTheme.colorScheme.primary)
                ) {

                    Icon(
                        painterResource(id = R.drawable.edit_icon),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .size(20.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .align(Alignment.Center)
                    )
                }
            }


            Spacer(modifier = Modifier.padding(20.dp))

            poppinsBoldText(
                contentText = "Full name",
                size = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 5.dp, 15.dp, 0.dp)
            )

            TextField(
                value = nameText,
                singleLine = true,
                onValueChange = { nameText = it },
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_medium))
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(
                        text = "Full name",
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_medium))
                    )
                },

                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxWidth()
                    .padding(20.dp, 10.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(50.dp))

            )

            Spacer(modifier = Modifier.padding(6.dp))

            poppinsBoldText(
                contentText = "Phone number",
                size = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 5.dp, 15.dp, 0.dp)
            )

            TextField(
                value = numberText,
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_medium))
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { numberText = it },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(
                        text = "Enter your number...",
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_medium))
                    )
                },

                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxWidth()
                    .padding(20.dp, 10.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(50.dp))
            )

        }


        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .wrapContentSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Row(
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(0.dp, 15.dp, 0.dp, 40.dp)
            ) {
                FullWidthButton(
                    label = "Update",
                    color = MaterialTheme.colorScheme.primary
                ) {
                    if (numberText.equals("")) {
                        snackBarMsg.value = "Please enter phone no"
                        openMySnackbar.value = true
                        return@FullWidthButton
                    }

                    scope.launch(Dispatchers.Main) {

                        profileViewModel.updateUserProfilePictureAndPhone(
                            imageUri.value,
                            nameText,
                            numberText,
                            sharedPreferences
                        ).collect {
                            when (it) {
                                is ResponseType.Error -> {
                                    showProgressBar.value = false
                                    snackBarMsg.value = it.errorMsg!!
                                    openMySnackbar.value = true
                                }

                                is ResponseType.Loading -> {
                                    showProgressBar.value = true
                                }

                                is ResponseType.Success -> {
                                    showProgressBar.value = false
                                    snackBarMsg.value = it.data!!
                                    openMySnackbar.value = true

                                    sharedPreferences.edit().putString("userName", nameText).apply()
                                    navController.popBackStack()
                                }
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