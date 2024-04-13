package com.chirag047.rapiddeliver.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun SubjectImage(image: Int,modifier: Modifier) {
    Image(
        painter = painterResource(id = image),
        contentDescription = "",
        modifier = modifier
    )
}