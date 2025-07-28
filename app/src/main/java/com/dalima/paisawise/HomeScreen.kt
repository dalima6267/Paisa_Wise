package com.dalima.paisawise

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.dalima.paisawise.data.ExpenseDao
import com.dalima.paisawise.db.HomeUIState
import com.dalima.paisawise.ui.theme.ButtonGreen
import com.dalima.paisawise.ui.theme.Gray
import com.dalima.paisawise.ui.theme.Green20
import com.dalima.paisawise.ui.theme.Lavender
import com.dalima.paisawise.ui.theme.LightGreen
import com.dalima.paisawise.ui.theme.LighterGreen
import com.dalima.paisawise.ui.theme.White40
import com.dalima.paisawise.viewmodel.HomeViewModel
import com.dalima.paisawise.viewmodel.HomeViewModelFactory
import androidx.compose.foundation.lazy.items
import com.dalima.paisawise.ui.theme.Cardgreen


@Composable
fun HomeScreen(expenseDao: ExpenseDao) {
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(expenseDao)
    )

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Create a local state to store the UI state
    var uiState by remember { mutableStateOf<HomeUIState>(HomeUIState.NoExpenses) }

    // Manually observe LiveData
    LaunchedEffect(Unit) {
        viewModel.uiState.observe(lifecycleOwner) {
            uiState = it
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        when (uiState) {
            is HomeUIState.NoExpenses -> {
                NoExpenseLayout()
            }
            is HomeUIState.HasExpenses -> {
                val state = uiState as HomeUIState.HasExpenses
                ExpenseLayout(
                    totalAmount = state.totalAmount,
                    comparison = state.comparisonText
                )
            }
        }

        BottomAppBar {
            // Add bottom bar actions if needed
        }
    }
}


@Composable
fun NoExpenseLayout() {
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
            onClick = { /* Navigate to Add Expense */ },
            colors = ButtonDefaults.buttonColors(backgroundColor = ButtonGreen)) {
            Text(text = "Add a new expense",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp)
        }
    }
}

@Composable
fun ExpenseLayout(totalAmount: Double, comparison: String) {
    val sampleTransactions = listOf(
        TransactionItem("Food", "Lunch at restaurant", 250.0),
        TransactionItem("Transport", "Uber ride", 150.0),
        TransactionItem("Entertainment", "Movie ticket", 300.0),
        TransactionItem("Shopping", "T-shirt from mall", 999.0),
        TransactionItem("Subscription", "Spotify Premium", 129.0)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp).background(White40)
    ) {
        Spacer(modifier = Modifier.height(58.dp))

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
                    Text("Total expense for July", fontWeight = FontWeight.Bold, fontSize = 26.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("\u20B9$totalAmount", fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(comparison, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Highest type: Food", fontSize = 16.sp)
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

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(
                onClick = { /* Transaction */ },
                modifier = Modifier
                    .width(170.dp)
                    .height(100.dp),
                colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Gray),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Transaction", color = Color.Black)
            }

            OutlinedButton(
                onClick = { /* Analysis */ },
                modifier = Modifier
                    .width(170.dp)
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
                .weight(1f), // take remaining space
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleTransactions) { transaction ->
                TransactionItemCard(transaction)
            }
        }
    }
}


data class TransactionItem(
    val category: String,
    val description: String,
    val amount: Double
)
@Composable
fun TransactionItemCard(transaction: TransactionItem) {
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
                    text = transaction.category,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = transaction.description,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
            Text(
                text = "₹${transaction.amount}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseLayout(){
    ExpenseLayout(totalAmount = 1000.0, comparison = "200 more than last month")
}
