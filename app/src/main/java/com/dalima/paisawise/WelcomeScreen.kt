package com.dalima.paisawise


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dalima.paisawise.ui.theme.DarkGreen
import com.dalima.paisawise.ui.theme.LightGreen

@Composable
fun WelcomeScreen(onGetStarted: () -> Unit, onSignIn: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGreen)
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "PaisaWise",
            fontSize = 30.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Image(painter = painterResource(id = R.drawable.person_with_money), contentDescription = null)
        Text(
            text = "Take Charge of Your Money\nThe Smart Way",
            fontSize = 18.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onGetStarted,
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkGreen, // background
                contentColor = Color.White // text color
            ),
            shape = RoundedCornerShape(12.dp) // optional: for rounded corners
        ) {
            Text("Get Started")
        }

        TextButton(
            onClick = onSignIn,
            colors = ButtonDefaults.textButtonColors(
                contentColor = Color.White // text color
            )
        ) {
            Text("Sign In")
        }

    }
}
