package com.dalima.paisawise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.dalima.paisawise.Enum.Screen
import com.dalima.paisawise.category.ExpenseCategoryScreen
import com.dalima.paisawise.db.PreferenceManager
import com.dalima.paisawise.navigatoon.MainScreen
import com.dalima.paisawise.onboarding.OnboardingScreen
import com.dalima.paisawise.signinprocess.SetupPinScreen
import com.dalima.paisawise.signinprocess.SignInScreen
import com.dalima.paisawise.signinprocess.SignUpScreen
import com.dalima.paisawise.splash.SplashScreen
import com.dalima.paisawise.splash.WelcomeScreen
import com.dalima.paisawise.ui.theme.PaisaWiseTheme
import com.dalima.paisawise.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PaisaWiseTheme {
                val navController = rememberNavController()
                val viewModel: AuthViewModel = viewModel()

                var isFirstLaunch by remember { mutableStateOf(PreferenceManager.isFirstLaunch(this@MainActivity)) }
                var isChecked by remember { mutableStateOf(false) }
                val savedPin = remember { mutableStateOf<String?>(null) }

                LaunchedEffect(Unit) {
                    savedPin.value = PinStorage.getSavedPin(this@MainActivity)
                }

                NavHost(navController = navController, startDestination = Screen.Splash.name) {

                    composable(Screen.Splash.name) {
                        SplashScreen {
                            if (isFirstLaunch) {
                                navController.navigate(Screen.Welcome.name) {
                                    popUpTo(Screen.Splash.name) { inclusive = true }
                                }
                            } else {
                                if (savedPin.value != null) {
                                    navController.navigate(Screen.SetupPin.name) { // verification mode
                                        popUpTo(Screen.Splash.name) { inclusive = true }
                                    }
                                } else {
                                    // No PIN yet, go to SignIn
                                    navController.navigate(Screen.SignIn.name) {
                                        popUpTo(Screen.Splash.name) { inclusive = true }
                                    }
                                }
                            }
                        }
                    }

                    composable(Screen.Welcome.name) {
                        WelcomeScreen(
                            onGetStarted = {
                                navController.navigate(Screen.Onboarding.name) {
                                    popUpTo(Screen.Welcome.name) { inclusive = true }
                                }
                            },
                            onSignIn = {
                                PreferenceManager.setFirstLaunchDone(this@MainActivity)
                                isFirstLaunch = false
                                navController.navigate(Screen.SignIn.name) {
                                    popUpTo(Screen.Welcome.name) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(Screen.Onboarding.name) {
                        OnboardingScreen {
                            PreferenceManager.setFirstLaunchDone(this@MainActivity)
                            isFirstLaunch = false
                            navController.navigate(Screen.SignUp.name) {
                                popUpTo(Screen.Onboarding.name) { inclusive = true }
                            }
                        }
                    }

                    composable(Screen.SignIn.name) {
                        SignInScreen(
                            navController = navController,
                            onSwitchClick = { navController.navigate(Screen.SignUp.name) },
                            viewModel = viewModel,
                            onLoginSuccess = {
                                // After login, go to SetupPin (setup mode)
                                navController.navigate(Screen.SetupPin.name) {
                                    popUpTo(Screen.SignIn.name) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(Screen.SignUp.name) {
                        SignUpScreen(
                            navController = navController,
                            onSwitchClick = { navController.navigate(Screen.SignIn.name) },
                            isChecked = isChecked,
                            onCheckedChange = { isChecked = it },
                            onPrivacyPolicyClick = {},
                            viewModel = viewModel,
                            onSignUpSuccess = {
                                // After signup, go to SetupPin (setup mode)
                                navController.navigate(Screen.SetupPin.name) {
                                    popUpTo(Screen.SignUp.name) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(Screen.SetupPin.name) {
                        SetupPinScreen(navController = navController)
                    }

                    composable(Screen.ExpenseCategory.name) {
                        ExpenseCategoryScreen(navController = navController)
                    }

                    composable(Screen.Main.name) {
                        MainScreen()
                    }
                }
            }
        }
    }
}

