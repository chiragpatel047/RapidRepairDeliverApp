package com.chirag047.rapiddeliver.Common

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chirag047.rapiddeliver.R

@Composable
fun NoDataText(text: String, isVisible: Boolean) {
    if(isVisible){
        Column(
            Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

}