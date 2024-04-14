package com.chirag047.rapiddeliver.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chirag047.rapiddeliver.R

@Composable
fun textBetweenTwoLines(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp, 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .weight(1f)
                .background(Color.LightGray)
        )
        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.poppins_medium)),
            modifier = Modifier
                .padding(20.dp, 5.dp)
        )
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .weight(1f)
                .background(Color.LightGray)
        )
    }
}


@Composable
fun textWithSeeAllText(title: String, onClick: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 10.dp, 20.dp, 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {


        Text(
            text = title,
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.poppins_medium))
        )

        Text(
            text = "See All",
            textAlign = TextAlign.Start,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary,
            fontFamily = FontFamily(Font(R.font.poppins_medium)),
            modifier = Modifier.clickable {
                onClick.invoke()
            }
        )

    }

}