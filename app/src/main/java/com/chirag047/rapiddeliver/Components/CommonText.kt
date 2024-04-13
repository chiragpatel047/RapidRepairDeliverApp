package com.chirag047.rapiddeliver.Components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.chirag047.rapiddeliver.R

@Composable
fun poppinsText(contentText: String, size: TextUnit, modifier: Modifier) {
    Text(
        text = contentText,
        fontFamily = FontFamily(Font(R.font.poppins_medium)),
        fontSize = size,
        modifier = modifier
    )
}

@Composable
fun poppinsCenterText(contentText: String, size: TextUnit, modifier: Modifier) {
    Text(
        text = contentText,
        textAlign = TextAlign.Center,
        fontFamily = FontFamily(Font(R.font.poppins_medium)),
        fontSize = size,
        modifier = modifier
    )
}

@Composable
fun poppinsBoldText(contentText: String, size: TextUnit, modifier: Modifier) {
    Text(
        text = contentText,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily(Font(R.font.poppins_medium)),
        fontSize = size,
        modifier = modifier
    )
}

@Composable
fun poppinsBoldCenterText(contentText: String, size: TextUnit, modifier: Modifier) {
    Text(
        text = contentText,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily(Font(R.font.poppins_medium)),
        fontSize = size,
        modifier = modifier
    )
}