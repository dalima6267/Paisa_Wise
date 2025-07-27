package com.dalima.paisawise.navigatoon

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.dalima.paisawise.AnalysisScreen
import com.dalima.paisawise.HomeScreen
import com.dalima.paisawise.db.AppDatabase
import com.dalima.paisawise.profileScreen.ProfileScreen
import com.dalima.paisawise.transactionScreen.TransactionScreen

@Composable
fun MainScreen() {

    val context = LocalContext.current
    val expenseDao = remember {
        AppDatabase.getDatabase(context).expenseDao()
    }
    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            CustomBottomBar(
                selectedIndex = selectedIndex,
                onItemSelected = { selectedIndex = it }
            )
        },
        backgroundColor = Color(0xFFF8FAFC) // backgroundColor for material.Scaffold
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedIndex) {
                0 -> HomeScreen(expenseDao = expenseDao)
                1 -> TransactionScreen()
                2 ->  AnalysisScreen(
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

@Composable
fun AddScreen() {
    Text("Add Screen")
}
