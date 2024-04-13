package com.chirag047.rapiddeliver.Components

import android.icu.text.CaseMap.Title
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chirag047.rapiddeliver.R

@Composable
fun customProgressBar(show: Boolean, title: String) {
    AnimatedVisibility(
        visible = show, enter = fadeIn(animationSpec = tween(400)),
        exit = fadeOut(animationSpec = tween(400))
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(Modifier.padding(20.dp))
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}