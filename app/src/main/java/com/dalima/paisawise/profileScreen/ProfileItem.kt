package com.dalima.paisawise.profileScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dalima.paisawise.R

@Composable
fun ProfileItem(
    icon:Int,
    title:String,
    value:String="",
    showArrow:Boolean=true,
    onClick: () -> Unit

){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically

    ){
        Icon(
            painter = painterResource(id = icon),
            contentDescription = title,
            tint = Color.Black,
            modifier = Modifier.padding(end = 16.dp).size(18.dp)

        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, modifier = Modifier.weight(1f), fontSize = 16.sp)
        if (value.isNotEmpty()){
            Text(value)
            Spacer(modifier = Modifier.width(16.dp))

        }
        if (showArrow) {
            Icon(
                painter = painterResource(id = R.drawable.arrowright),
                contentDescription = "Arrow"
            )
        }
    }
}