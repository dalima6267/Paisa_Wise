package com.dalima.paisawise.navigatoon

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dalima.paisawise.*
import com.dalima.paisawise.category.ExpenseCategoryScreen
import com.dalima.paisawise.data.Expense
import com.dalima.paisawise.db.AppDatabase
import com.dalima.paisawise.db.HomeUIState
import com.dalima.paisawise.profileScreen.ProfileScreen
import com.dalima.paisawise.transactionScreen.TransactionScreen
import com.dalima.paisawise.ui.theme.Black
import com.dalima.paisawise.viewmodel.CategoryViewModel
import com.dalima.paisawise.viewmodel.HomeViewModel
import com.dalima.paisawise.viewmodel.HomeViewModelFactory
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MainScreen(
    viewModel: CategoryViewModel = viewModel()
) {
    var isDarkMode by remember { mutableStateOf(false) }
    var selectedMonth by rememberSaveable{
        mutableStateOf(
            SimpleDateFormat("MMMM", Locale.getDefault()).format(Date())
        )
    }
    AppTheme(darkTheme = isDarkMode) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val expenseDao = remember { AppDatabase.getDatabase(context).expenseDao() }
    var selectedIndex by remember { mutableStateOf(0) }
    val selectedTags by viewModel.selectedTags.collectAsState()
    val systemUiController = rememberSystemUiController()
    val categoryViewModel: CategoryViewModel = viewModel()
    var showDialog by remember { mutableStateOf(false) }
    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(expenseDao)
    )

    // Track current route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Set status bar
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Black,
            darkIcons = true
        )
    }

    Column {
        // Show TopBar only when not in ProfileScreen
        if (currentRoute != "ProfileScreen") {
            Box(
                modifier = Modifier
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .zIndex(1f)
            ) {
                TopBarWithMonthPicker(
                    onNotificationClick = { /* TODO */ },
                    onMonthSelected = { month ->
                        selectedMonth = month
                        homeViewModel.setSelectedMonth(month)
                    },
                    onProfileClick = {
                        navController.navigate("ProfileScreen")
                    }
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

                            4 -> {
                                showDialog = true // Show dialog instead of navigating
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
                        HomeScreen(navController = navController, expenseDao = expenseDao, selectedMonth = selectedMonth)
                    }
                    composable("TransactionScreen") {
                        TransactionScreen(navController = navController, expenseDao = expenseDao, selectedMonth = selectedMonth)
                    }
                    composable("AnalysisScreen") {
                        val uiState by homeViewModel.uiState.observeAsState(HomeUIState.NoExpenses)
                        val categoryExpenses by homeViewModel.expensesByCategory.observeAsState(
                            initial = emptyMap()
                        )
                        when (uiState) {
                            is HomeUIState.HasExpenses -> {
                                val state = uiState as HomeUIState.HasExpenses
                                AnalysisScreen(
                                    totalExpense = state.totalAmount,
                                    expensesByType = categoryExpenses,
                                    expenseDao = expenseDao,
                                    selectedMonth = selectedMonth
                                )
                            }

                            is HomeUIState.NoExpenses -> {
                                AnalysisScreen(
                                    totalExpense = 0.0,
                                    expensesByType = emptyMap(),
                                    expenseDao = expenseDao,
                                    selectedMonth = selectedMonth
                                )
                            }
                        }
                    }
                    composable("ProfileScreen") {

                        ProfileScreen(
                            isDarkMode = isDarkMode,
                            onDarkModeChange = { isDarkMode = it }
                        )

                    }
                    composable("ExpenseCategoryScreen") {
                        ExpenseCategoryScreen(navController)
                    }
                }
            }
        }
    }

    // Show Add Expense Dialog
    if (showDialog) {
        var showCategoryPicker by remember { mutableStateOf(false) }
        var selectedCategoryName by remember { mutableStateOf("") }
        var selectedCategoryIcon by remember { mutableStateOf<Int?>(null) }

        AddExpenseDialog(
            categories = categoryViewModel.categories,
            onDismiss = { showDialog = false },
            onSave = { categoryName, iconRes, title, amount, date, description, invoice ->
                homeViewModel.addExpense(
                    Expense(
                        category = categoryName,
                        categoryIconRes = iconRes,
                        title = title,
                        amount = amount.toDoubleOrNull() ?: 0.0,
                        date = date,
                        description = description,
                        invoice = invoice
                    )
                )
                showDialog = false
            }
        )

        if (showCategoryPicker) {
            androidx.compose.ui.window.Dialog(onDismissRequest = { showCategoryPicker = false }) {
                androidx.compose.material.Card(
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                    backgroundColor = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                ) {
                    ExpenseCategoryScreen(
                        onCategorySelected = { category ->
                            selectedCategoryName = category.name
                            selectedCategoryIcon = category.iconRes
                            showCategoryPicker = false
                        }
                    )
                }
            }
        }
    }
}
}
