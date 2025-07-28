package com.dalima.paisawise.navigatoon

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import com.dalima.paisawise.AnalysisScreen
import com.dalima.paisawise.HomeScreen
import com.dalima.paisawise.db.AppDatabase
import com.dalima.paisawise.profileScreen.ProfileScreen
import com.dalima.paisawise.transactionScreen.TransactionScreen
import com.dalima.paisawise.ui.theme.Black
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val expenseDao = remember { AppDatabase.getDatabase(context).expenseDao() }
    var selectedIndex by remember { mutableStateOf(0) }

    val systemUiController = rememberSystemUiController()

    // Set status bar background and icons color
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Black, // Same as TopBar background
            darkIcons = true // ✅ Black icons
        )
    }
    Column {
        // Show TopBar for all screens except Profile (index 3)
        if (selectedIndex != 3) {
            Box(
                modifier = Modifier
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .zIndex(1f) // ensure it stays on top
            ) {
                TopBarWithMonthPicker(
                    onNotificationClick = { /* TODO */ },
                    onMonthSelected = { month -> /* handle month */ }
                )
            }
        }

        Scaffold(
            bottomBar = {
                CustomBottomBar(
                    selectedIndex = selectedIndex,
                    onItemSelected = { selectedIndex = it }
                )
            },
            backgroundColor = Color(0xFFF8FAFC)
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when (selectedIndex) {
                    0 -> HomeScreen(expenseDao = expenseDao)
                    1 -> TransactionScreen()
                    2 -> AnalysisScreen(
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
                    3 -> ProfileScreen()
                    4 -> AddScreen()
                }
            }
        }
    }
}


@Composable
fun AddScreen() {
    Text("Add Screen")
}
