package com.dalima.paisawise

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dalima.paisawise.ui.theme.Green20
import com.dalima.paisawise.ui.theme.LighterGreen

@Composable
fun SocialLoginButton(iconRes: Int){
    Box(
        modifier = Modifier
            .size(width = 70.dp, height = 35.dp)
            .clip(RoundedCornerShape(20))
            .background(Green20)
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
