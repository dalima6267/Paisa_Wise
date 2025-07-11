package com.dalima.paisawise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dalima.paisawise.ui.theme.LightGreen
import com.dalima.paisawise.ui.theme.LighterGreen
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGreen)
    ) {
        // Pager (fills the screen)
        HorizontalPager(
            count = onboardingPages.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            OnboardingPageView(onboardingPages[page])
        }

        // Dot Indicator - centered at bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 70.dp)
        ) {
            HorizontalPagerIndicator(
                pagerState = pagerState,
                activeColor = Color.White,
                inactiveColor = Color.LightGray
            )
        }

        // Arrow Button - bottom end
        FloatingActionButton(
            onClick = {
                if (pagerState.currentPage == onboardingPages.lastIndex) {
                    onFinished()
                } else {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            },
            containerColor = LighterGreen,
            contentColor = Color.Black,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(60.dp)
                .width(50.dp)
                .height(35.dp)
        ) {
            Icon(Icons.Default.ArrowForward, contentDescription = "Next")
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OnboardingScreenPreview(){
    OnboardingScreen(onFinished = {})
}
