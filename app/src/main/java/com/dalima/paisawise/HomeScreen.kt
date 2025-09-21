package com.dalima.paisawise
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.dalima.paisawise.data.ExpenseDao
import com.dalima.paisawise.db.HomeUIState
import com.dalima.paisawise.ui.theme.ButtonGreen
import com.dalima.paisawise.viewmodel.HomeViewModel
import com.dalima.paisawise.viewmodel.HomeViewModelFactory
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.dalima.paisawise.category.Category
import com.dalima.paisawise.data.Expense
import com.dalima.paisawise.viewmodel.CategoryViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun HomeScreen(navController: NavHostController, expenseDao: ExpenseDao) {
    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(expenseDao)
    )
    val categoryViewModel: CategoryViewModel = viewModel()

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Create a local state to store the UI state
    var showDialog by remember{mutableStateOf(false)}
    val transactions by homeViewModel.allExpenses.observeAsState(emptyList())

    // Manually observe LiveData
    val uiState by homeViewModel.uiState.observeAsState(HomeUIState.NoExpenses)


    val categories by categoryViewModel.selectedTags.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {

        when (uiState) {
            is HomeUIState.NoExpenses -> {
                NoExpenseLayout( onAddClick = {showDialog=true})
            }
            is HomeUIState.HasExpenses -> {
                val state = uiState as HomeUIState.HasExpenses
                ExpenseLayout(
                    navController = navController,
                    onAddClick = { showDialog = true },
                    totalAmount = state.totalAmount,
                    lastMonthAmount = state.lastMonthAmount,
                    todayAmount = state.todayAmount,
                    comparison = state.comparisonText,
                    highestCategory = state.highestCategory,
                    currentMonth = state.currentMonthName,
                    transactions = transactions
                )
            }
        }
        if (showDialog) {
            AddExpenseDialog(
                categories = categoryViewModel.categories,
                onDismiss = { showDialog = false },
                onSave = { category, iconRes, title, amount, date, description, invoice ->
                    homeViewModel.addExpense(
                        Expense(
                            category = category,
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
        }

    }
}


@Composable
fun NoExpenseLayout(onAddClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.noexpense),
            contentDescription = "No Expense",
            modifier = Modifier.size(250.dp)
        )
        Spacer(modifier = Modifier.height(28.dp))
        Text(text = "No expense added yet",
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = Color.Black)
        Spacer(modifier = Modifier.height(28.dp))
        Button(
            onClick = onAddClick,
            colors = ButtonDefaults.buttonColors(backgroundColor = ButtonGreen)) {
            Text(text = "Add a new expense",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp)
        }
    }
}

@Composable
fun ExpenseLayout( navController: NavController,
                   onAddClick: () -> Unit,
                   totalAmount: Double,
                   lastMonthAmount: Double,
                   todayAmount: Double,
                   comparison: String,
                   highestCategory: String,
                   currentMonth: String,
                   transactions: List<Expense>
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
        .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            backgroundColor = Color(0xFF2FB771),
            elevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .shadow(
                    elevation = 26.dp,
                    shape = RoundedCornerShape(20.dp),
                    clip = false
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text("This Month", color = Color.White, fontSize = 16.sp)
                        Text(
                            "₹${"%.2f".format(totalAmount)}",
                            color = Color.White,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(comparison, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)                    }
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color.White.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(50.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = currentMonth,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_last_month), 
                                contentDescription = "Last Month Icon",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Last Month", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)

                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "₹${"%.2f".format(lastMonthAmount)}",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_today), // your icon here
                                contentDescription = "Today Icon",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Today", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)

                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "₹${"%.2f".format(todayAmount)}",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Quick Actions",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            QuickActionButton(
                "Transactions",
                R.drawable.ic_transaction,
                onClick = { navController.navigate("TransactionScreen") },
                modifier = Modifier.weight(1f)
            )
            QuickActionButton(
                "Analysis",
                R.drawable.ic_analysis,
                onClick = { navController.navigate("AnalysisScreen") },
                modifier = Modifier.weight(1f)
            )
        }


        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text("Recent Transactions", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Text(
                text = "See all",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.clickable {
                    navController.navigate("TransactionScreen")
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(transactions.take(5)) { expense ->
                TransactionItemCard(expense)
            }
        }
    }
}
@Composable
fun QuickActionButton(
    title: String,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(30.dp),
        elevation = 2.dp,
        backgroundColor = Color.Transparent,
        modifier = modifier
            .height(90.dp)
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(30.dp),
                clip = false
            )
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2FB771),
                            Color(0xFF008E6B)
                        )
                    ),
                    shape = RoundedCornerShape(30.dp)
                )
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = title,
                    modifier = Modifier.size(44.dp),
                    colorFilter = ColorFilter.tint(Color.White) // 🔹 behaves like Icon tint
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(title, fontSize = 16.sp, color = Color.White)
            }
        }
    }
}


@Composable
fun TransactionItemCard(expense: Expense) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = expense.categoryIconRes),
                contentDescription = expense.category,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(expense.category, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text(expense.date, fontSize = 12.sp, color = Color.Gray)
            }
        }
        Text(
            text = "₹${expense.amount}",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.Red
        )
    }
}

@Composable
fun AddExpenseDialog(
    categories: List<Category>,
    onDismiss: () -> Unit,
    onSave: (
        category: String,
        categoryIconRes: Int,
        title: String,
        amount: String,
        date: String,
        description: String,
        invoice: String
    ) -> Unit) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedIconRes by remember { mutableStateOf(0) }
    var description by remember { mutableStateOf("") }
    var invoiceFile by remember { mutableStateOf("") }

    val currentDate = remember {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        formatter.format(Date())
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            backgroundColor = Color(0xFFE9F3F2),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(12.dp))
                ) {
                    Text(
                        text = "Add New Expense",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }



                Spacer(modifier = Modifier.height(16.dp))

                // Title
                LabeledTextField(
                    label = "Title",
                    value = title,
                    placeholder = "ex: Train ticket to Raipur",
                    onValueChange = { title = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Category & Date
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Category
                    Box(modifier = Modifier.weight(1f)) {
                        Column {
                            Text(
                                text = "Category",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(36.dp)
                                    .background(Color.White, RoundedCornerShape(8.dp))
                                    .border(1.dp, Color(0xFF34AC90), RoundedCornerShape(8.dp))
                                    .clickable { expanded = true },
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = selectedCategory?.name ?: "Select Category",
                                    color = if (selectedCategory == null) Color.Gray else Color.Black,
                                    fontSize = 14.sp,
                                    style = TextStyle(
                                        lineHeight = 4.sp   // 🔹 Match with fontSize
                                    ),
                                    modifier = Modifier.padding(start = 12.dp)
                                )


                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    categories.forEach { cat ->
                                        DropdownMenuItem(onClick = {
                                            selectedCategory = cat
                                            expanded = false
                                        }) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Image(
                                                    painter = painterResource(id = cat.iconRes),
                                                    contentDescription = cat.name,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(cat.name)
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }

                    // Date
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Date",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(36.dp)
                                .background(Color.White, RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFF34AC90), RoundedCornerShape(8.dp))
                                .clickable { /* TODO: open date picker */ },
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                currentDate,
                                color = Color.Black,
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Amount
                LabeledTextField(
                    label = "Amount",
                    value = amount,
                    placeholder = "ex: 512",
                    onValueChange = { amount = it },
                    leading = { Text("₹", fontWeight = FontWeight.Bold) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Description
                LabeledTextField(
                    label = "Description (Optional)",
                    value = description,
                    placeholder = "ex: The ticket was booked",
                    onValueChange = { description = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Invoice Upload
                Column {
                    Text(
                        text = "Invoice",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedButton(
                        onClick = { /* Open file/image picker */ },
                        modifier = Modifier.fillMaxWidth().height(36.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFF34AC90)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(if (invoiceFile.isEmpty()) "Choose File / Take Picture" else invoiceFile)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (selectedCategory != null) {
                            onSave(
                                selectedCategory!!.name,
                                selectedCategory!!.iconRes,
                                title,
                                amount,
                                currentDate,
                                description,
                                invoiceFile
                            )
                        }                    },
                    modifier = Modifier.fillMaxWidth().height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = ButtonGreen)
                ) {
                    Text("Add Expense", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun LabeledTextField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    leading: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val focusRequester = remember { FocusRequester() }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .border(1.dp, Color(0xFF34AC90), RoundedCornerShape(8.dp))
                .clickable {
                    // 🔹 Request focus when anywhere in the box is touched
                    focusRequester.requestFocus()
                },
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                if (leading != null) {
                    leading()
                    Spacer(modifier = Modifier.width(4.dp))
                }

                // Show placeholder when empty
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }

                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true,
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester) // 🔹 Attach FocusRequester
                )
            }
        }
    }
}






