package com.dalima.paisawise.splash


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dalima.paisawise.R
import com.dalima.paisawise.ui.theme.DarkGreen
import com.dalima.paisawise.ui.theme.LightGreen

val HammerSmithone = FontFamily(
    Font(R.font.hammersmithone_regular)
)
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
        Spacer(modifier = Modifier.height(80.dp))
        Text(
            text = "PaisaWise",
            fontSize = 40.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Image(painter = painterResource(id = R.drawable.person_with_money), contentDescription = null)
        Text(
            text = "Take Charge of Your Money\nThe Smart Way",
            fontSize = 20.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(110.dp))

        Button(
            onClick = onGetStarted,
            modifier = Modifier
                .width(220.dp)
                .height(50.dp)
                .shadow(
                    elevation = 26.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = Color.Green.copy(alpha = 0f),
                    spotColor = Color.Green.copy(alpha = 0f)
                ),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkGreen,
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 12.dp,
                pressedElevation = 16.dp,
                focusedElevation = 16.dp,
                hoveredElevation = 16.dp
            )
        ) {
            Text("Get Started", fontSize = 18.sp, fontFamily = HammerSmithone, fontWeight = FontWeight.Normal)
        }

        TextButton(
            onClick = onSignIn,
            colors = ButtonDefaults.textButtonColors(
                contentColor = Color.White
            )
        ) {
            Text("Sign In",
                fontSize = 16.sp,
                style = LocalTextStyle.current.copy(
                textDecoration = TextDecoration.Underline
            ))
        }

    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(
        onGetStarted = {},
        onSignIn = {}
    )
}