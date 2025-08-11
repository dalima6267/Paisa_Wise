package com.dalima.paisawise

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController

@Composable
fun AddScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        navController.navigate("ExpenseCategoryScreen") {
            popUpTo("HomeScreen") // Optional: to manage backstack
        }
    }
}