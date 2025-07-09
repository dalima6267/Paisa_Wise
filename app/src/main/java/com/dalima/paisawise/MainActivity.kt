package com.dalima.paisawise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dalima.paisawise.ui.theme.PaisaWiseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PaisaWiseTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Screen.Splash.name) {
                    composable(Screen.Splash.name) {
                        SplashScreen { navController.navigate(Screen.Welcome.name) }
                    }
                    composable(Screen.Welcome.name) {
                        WelcomeScreen(
                            onGetStarted = { navController.navigate(Screen.Onboarding.name) },
                            onSignIn = { navController.navigate(Screen.SignIn.name) }
                        )
                    }
                    composable(Screen.Onboarding.name) {
                        OnboardingScreen { navController.navigate(Screen.SignIn.name) }
                    }
                    composable(Screen.SignIn.name) {
                        SignInScreen()
                    }
                }
            }
        }
    }}
