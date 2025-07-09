package com.dalima.paisawise

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
            .background(LightGreen),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            painter = painterResource(id = page.imageRes),
            contentDescription = null,
            modifier = Modifier.size(250.dp)
        )
        Text(
            text = page.title,
            style = MaterialTheme.typography.titleLarge,
            color = White40,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyMedium,
            color = Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}
val onboardingPages = listOf(
    OnboardingPage(
        title = "Gain total control of your money",
        description = "Become your own money manager and make every cent count",
        imageRes = R.drawable.onboarding_2_paisawise
    ),
    OnboardingPage(
        title = "Know where your money goes",
        description = "Track your transaction easily, with categories and financial report",
        imageRes = R.drawable.onboarding_2_paisawise
    ),
    OnboardingPage(
        title = "Plan ahead\nStay on track",
        description = "Setup your budget for each category and stay in control",
        imageRes = R.drawable.onboarding_3_paisawise
    )
)