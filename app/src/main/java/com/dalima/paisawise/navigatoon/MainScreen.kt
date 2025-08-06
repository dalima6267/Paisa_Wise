package com.dalima.paisawise.navigatoon

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dalima.paisawise.AnalysisScreen
import com.dalima.paisawise.HomeScreen
import com.dalima.paisawise.db.AppDatabase
import com.dalima.paisawise.profileScreen.ProfileScreen
import com.dalima.paisawise.transactionScreen.TransactionScreen
import com.dalima.paisawise.ui.theme.Black
import com.dalima.paisawise.viewmodel.CategoryViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun MainScreen(
    viewModel: CategoryViewModel = viewModel()
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val expenseDao = remember { AppDatabase.getDatabase(context).expenseDao() }
    var selectedIndex by remember { mutableStateOf(0) }
    val selectedTags by viewModel.selectedTags.collectAsState()
    val systemUiController = rememberSystemUiController()

    // Set status bar
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Black,
            darkIcons = true
        )
    }

    Column {
        if (selectedIndex != 3) {
            Box(
                modifier = Modifier
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .zIndex(1f)
            ) {
                TopBarWithMonthPicker(
                    onNotificationClick = { /* TODO */ },
                    onMonthSelected = { month -> /* TODO */ }
                )
            }
        }

        Scaffold(
            bottomBar = {
                CustomBottomBar(
                    selectedIndex = selectedIndex,
                    onItemSelected = { index ->
                        selectedIndex = index
                        when (index) {
                            0 -> navController.navigate("HomeScreen") {
                                popUpTo("HomeScreen") { inclusive = true }
                                launchSingleTop = true
                            }
                            1 -> navController.navigate("TransactionScreen") {
                                popUpTo("HomeScreen")
                                launchSingleTop = true
                            }
                            2 -> navController.navigate("AnalysisScreen") {
                                popUpTo("HomeScreen")
                                launchSingleTop = true
                            }
                            3 -> navController.navigate("ProfileScreen") {
                                popUpTo("HomeScreen")
                                launchSingleTop = true
                            }
                            4 -> navController.navigate("AddScreen") {
                                popUpTo("HomeScreen")
                                launchSingleTop = true
                            }
                        }
                    }
                )
            },
            backgroundColor = Color(0xFFF8FAFC)
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                NavHost(
                    navController = navController,
                    startDestination = "HomeScreen"
                ) {
                    composable("HomeScreen") {
                        HomeScreen(navController = navController, expenseDao = expenseDao)
                    }
                    composable("TransactionScreen") {
                        TransactionScreen(navController = navController, expenseDao = expenseDao)
                    }
                    composable("AnalysisScreen") {
                        AnalysisScreen(
                            analysisSummary = "This month you spent the most on Food. Consider reducing it next month.",
                            expensesByType = mapOf(
                                "Food" to 5000f,
                                "Travel" to 2500f,
                                "Shopping" to 3500f,
                                "Bills" to 1200f
                            ),
                            onDownloadClick = { /* TODO */ },
                            onPdfGenerateClick = { /* TODO */ }
                        )
                    }
                    composable("ProfileScreen") {
                        ProfileScreen()
                    }
                    composable("AddScreen") {
                        AddScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun AddScreen() {
    Text("Add Screen")
}
