package com.chirag047.rapiddeliver.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chirag047.rapiddeliver.R

@Composable
fun FullWidthButton(label: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(color),
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp, 0.dp)
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.poppins_medium)),
            modifier = Modifier
                .padding(5.dp)
        )
    }
}

@Composable
fun FullWidthTransparentButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onBackground),
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp, 0.dp)
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.poppins_medium)),
            modifier = Modifier
                .padding(5.dp)
        )
    }
}

@Composable
fun OutlinedCustomButton(imageIcon: Int, alpha: Float, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .size(50.dp)
            .alpha(alpha = alpha),  //avoid the oval shape
        shape = CircleShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        contentPadding = PaddingValues(15.dp),  //avoid the little icon
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
    ) {
        Icon(
            painterResource(id = imageIcon),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun FilledCustomButton(imageIcon: Int, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(50.dp)
            .clip(RoundedCornerShape(50.dp)),  //avoid the oval shape
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Icon(
            painterResource(id = imageIcon),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .size(50.dp)
                .padding(15.dp)
                .rotate(180F)
        )
    }
}



@Composable
fun FilledCommonCustomButton(imageIcon: Int, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(35.dp)
            .clip(RoundedCornerShape(30.dp)),  //avoid the oval shape
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Icon(
            painterResource(id = imageIcon),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier
                .size(35.dp)
                .padding(8.dp)
        )
    }
}

@Composable
fun GrayFilledSimpleButton(imageIcon: Int, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .size(40.dp),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Icon(
            painterResource(id = imageIcon),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier
                .size(40.dp)
                .padding(10.dp)
        )
    }
}