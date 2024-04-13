package com.chirag047.rapiddeliver.Components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chirag047.rapiddeliver.R


@Composable
fun ActionBarWIthBack(title : String){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(15.dp, 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.down_arrow_icon),
            contentDescription = "",
            modifier = Modifier
                .size(30.dp)
                .padding(5.dp)
                .rotate(90f),
            tint = MaterialTheme.colorScheme.onBackground
        )
        poppinsBoldCenterText(
            contentText = title,
            size = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}