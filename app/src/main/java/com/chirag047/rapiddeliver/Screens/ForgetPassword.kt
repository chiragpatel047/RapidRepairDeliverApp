package com.chirag047.rapiddeliver.Screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chirag047.rapiddeliver.Common.ResponseType
import com.chirag047.rapiddeliver.Components.FullWidthButton
import com.chirag047.rapiddeliver.Components.SnackbarWithoutScaffold
import com.chirag047.rapiddeliver.Components.SubjectImage
import com.chirag047.rapiddeliver.Components.customProgressBar
import com.chirag047.rapiddeliver.Components.poppinsBoldCenterText
import com.chirag047.rapiddeliver.Components.poppinsCenterText
import com.chirag047.rapiddeliver.Components.textBetweenTwoLines
import com.chirag047.rapiddeliver.R
import com.chirag047.rapiddeliver.Viewmodels.ForgetPasswordViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgetPassword(navController: NavController) {
    val forgetPasswordViewModel: ForgetPasswordViewModel = hiltViewModel()

    val scope = rememberCoroutineScope()

    val showProgressBar = remember {
        mutableStateOf(false)
    }

    var openMySnackbar = remember { mutableStateOf(false) }
    var snackBarMsg = remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {

        Column() {

            Spacer(modifier = Modifier.padding(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                SubjectImage(
                    image = R.drawable.forgetpassword_subject_image,
                    Modifier.padding(40.dp, 0.dp)
                )
            }

            poppinsBoldCenterText(
                contentText = "Reset link at your inbox",
                22.sp,
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            )

            poppinsCenterText(
                contentText = "Please, enter your email address. you will receive a link to reset your password via email.",
                size = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 5.dp, 15.dp, 15.dp)
            )

            textBetweenTwoLines(text = "Forget Password")

            var emailText by remember { mutableStateOf("") }

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                TextField(
                    value = emailText,
                    singleLine = true,
                    onValueChange = { emailText = it },
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
                            text = "Email",
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

            Spacer(modifier = Modifier.padding(10.dp))

            FullWidthButton(label = "Send email", MaterialTheme.colorScheme.primary) {
                if (emailText.isEmpty()) {
                    snackBarMsg.value = "Email can't be empty"
                    openMySnackbar.value = true
                    return@FullWidthButton
                }

                scope.launch(Dispatchers.Main) {
                    forgetPasswordViewModel.sendEmailPasswordResetLink(emailText).collect {
                        when (it) {
                            is ResponseType.Success -> {
                                showProgressBar.value = false
                                snackBarMsg.value = it.data!!
                                openMySnackbar.value = true
                            }

                            is ResponseType.Error -> {
                                showProgressBar.value = false
                                snackBarMsg.value = it.errorMsg.toString()
                                openMySnackbar.value = true
                            }

                            is ResponseType.Loading -> {
                                showProgressBar.value = true
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.padding(40.dp))
        }

        customProgressBar(show = showProgressBar.value, title = "Wait a moment...")

        SnackbarWithoutScaffold(
            snackBarMsg.value, openMySnackbar.value, { openMySnackbar.value = it }, Modifier.align(
                Alignment.BottomCenter
            )
        )
    }

}