package com.dalima.paisawise

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dalima.paisawise.ui.theme.Gray
import com.dalima.paisawise.ui.theme.LightGreen
import com.dalima.paisawise.ui.theme.White40

@Composable
fun OnboardingPageView(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp)
            .background(LightGreen),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Image(
            painter = painterResource(id = page.imageRes),
            contentDescription = null,
            modifier = Modifier.size(250.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = page.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = White40,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Text(
                text = page.description,
                fontSize = 14.sp,
                color = Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(200.dp))
    }
}
val onboardingPages = listOf(
    OnboardingPage(
        title = "Gain total control\nof your money",
        description = "Become your own money manager\nand make every cent count",
        imageRes = R.drawable.onboarding_2_paisawise
    ),
    OnboardingPage(
        title = "Know where your\nmoney goes",
        description = "Track your transaction easily,\nwith categories and financial report",
        imageRes = R.drawable.onboarding_2_paisawise
    ),
    OnboardingPage(
        title = "Plan ahead\nStay on track",
        description = "Setup your budget for each\ncategory and stay in control",
        imageRes = R.drawable.onboarding_3_paisawise
    )
)