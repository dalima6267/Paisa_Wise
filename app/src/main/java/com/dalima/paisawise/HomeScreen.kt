package com.dalima.paisawise
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
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
import com.dalima.paisawise.ui.theme.Gray
import com.dalima.paisawise.ui.theme.Lavender
import com.dalima.paisawise.ui.theme.White40
import com.dalima.paisawise.viewmodel.HomeViewModel
import com.dalima.paisawise.viewmodel.HomeViewModelFactory
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.dalima.paisawise.data.Expense
import com.dalima.paisawise.ui.theme.Cardgreen
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
                    onAddClick = {showDialog=true},
                    totalAmount = state.totalAmount,
                    comparison = state.comparisonText,
                    highestCategory = state.highestCategory,
                    currentMonth = state.currentMonthName,
                    transactions = transactions
                )
            }
        }
        if(showDialog){
            AddExpenseDialog(
                categories=categories,
                onDismiss={showDialog=false},
                onSave={
                    category, title, amount, date ->
                    homeViewModel.addExpense(
                        Expense(
                            category = category,
                            title = title,
                            amount = amount.toDoubleOrNull() ?: 0.0,
                            date = date
                        )
                    )
                    showDialog=false
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
fun ExpenseLayout(navController: NavController,onAddClick: () -> Unit,totalAmount: Double, comparison: String,highestCategory: String,
                  currentMonth: String,transactions: List<Expense>
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
        .padding(10.dp).background(White40)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 8.dp,
            shape = RoundedCornerShape(10.dp),
            backgroundColor = Cardgreen
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {


                Column {
                    Text("Total expense for $currentMonth", fontWeight = FontWeight.Bold, fontSize = 26.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("\u20B9$totalAmount", fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(comparison, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Highest type: $highestCategory", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.width(30.dp))
                Image(
                    painter = painterResource(id = R.drawable.monthlyexpense), // Replace with your drawable
                    contentDescription = "Expense Icon",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(end = 16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        Text("Quick Links", fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = {  navController.navigate("TransactionScreen") },
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Gray),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Transaction", color = Color.Black)
            }

            OutlinedButton(
                onClick = { navController.navigate("AnalysisScreen") },
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Gray),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Get analysis report", color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Recent Transactions", fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(12.dp))

        // ✅ LazyColumn below heading like RecyclerView
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ✅ Only show two transaction items
            items(transactions.take(3)) { transaction ->
                TransactionItemCard(transaction)
            }
        }

        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Show More....",
            modifier = Modifier
                .align(Alignment.End)
                .clickable {
                    // Navigate to another fragment
                    navController.navigate("TransactionScreen")
                },
            color = Color.Blue,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = onAddClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = ButtonGreen)
        ) {
            Text(
                text = "Add a new expense",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}
@Composable
fun TransactionItemCard(expense: Expense) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp,
        backgroundColor = Lavender
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = expense.category,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = expense.title,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
            Text(
                text = "₹${expense.amount}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}
@Composable
fun AddExpenseDialog(
    categories: List<String>,
    onDismiss: () -> Unit,
    onSave: (category: String, title: String, amount: String, date:String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull() ?: "") }

    val currentDate=remember{
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        formatter.format(Date())
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Expense") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title/Description") }
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Box {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        label = { Text("Category") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        }
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
                                Text(cat)
                            }
                        }
                    }
                }
                // Show the current date as read-only
                OutlinedTextField(
                    value = currentDate,
                    onValueChange = {},
                    label = { Text("Date") },
                    readOnly = true
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(selectedCategory, title, amount,currentDate)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


