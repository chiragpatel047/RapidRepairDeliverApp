package com.chirag047.rapiddeliver.Components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chirag047.rapiddeliver.R
import kotlinx.coroutines.launch

@Composable
fun SnackbarWithoutScaffold(
    message: String,
    showSb: Boolean,
    openSnackbar: (Boolean) -> Unit,
    modifier: Modifier
) {

    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()

    SnackbarHost(
        modifier = modifier,
        hostState = snackState
    ) {
        Snackbar(
            modifier = Modifier.padding(8.dp),
            containerColor = MaterialTheme.colorScheme.onBackground
        ) {
            Row() {
                Icon(
                    Icons.Filled.Warning,
                    "",
                    Modifier.padding(end = 8.dp),
                    tint = MaterialTheme.colorScheme.background
                )

                Text(
                    text = message,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_medium))
                    ),
                    modifier = Modifier.padding(0.dp, 1.dp, 0.dp, 0.dp)
                )
            }
        }
    }

    if (showSb) {
        LaunchedEffect(Unit) {
            snackScope.launch { snackState.showSnackbar(message) }
            openSnackbar(false)
        }
    }
}