package com.dalima.paisawise

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dalima.paisawise.ui.theme.LighterGreen
import com.dalima.paisawise.ui.theme.White40

@Composable
fun SocialLoginButton(iconRes: Int){
    Box(
        modifier = Modifier
            .size(44.dp)
            .background(LighterGreen)

            .clickable { },
        contentAlignment = Alignment.Center
    ){
        Icon(
            painter = painterResource(id =  iconRes),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color.Unspecified
        )
    }
}
