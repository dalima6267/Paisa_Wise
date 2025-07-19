package com.dalima.paisawise.navigatoon

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dalima.paisawise.profileScreen.ProfileScreen
import com.dalima.paisawise.transactionScreen.TransactionScreen

@Composable
fun MainScreen() {
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
                0 -> HomeScreen()
                1 -> TransactionScreen()
                2 -> StatsScreen()
                3 -> ProfileScreen()
                4 -> AddScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Text("Home Screen")
}

@Composable
fun StatsScreen() {
    Text("Stats Screen")
}


@Composable
fun AddScreen() {
    Text("Add Screen")
}
