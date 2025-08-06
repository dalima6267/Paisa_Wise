package com.dalima.paisawise.transactionScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dalima.paisawise.R
import com.dalima.paisawise.data.Expense
import com.dalima.paisawise.ui.theme.White40

@Composable
fun TransactionCardItem(
    expense: Expense,
    onDelete: () -> Unit,
    backgroundColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = 6.dp,
        backgroundColor = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_info_details),
                    contentDescription = "Category Icon",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 12.dp)
                        .background(White40, shape = CircleShape)
                        .clip(CircleShape)
                        .border(2.dp, Color.Black, CircleShape),
                    tint = Color.Unspecified
                )

                Column {
                    Text(
                        text = expense.date,
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                    Text(
                        text = expense.category,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Text(
                        text = expense.title,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "₹${expense.amount}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete),
                        contentDescription = "Delete",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

// Card color palette
val cardColors = listOf(
    Color(0xFFE3F2FD), // Light Blue
    Color(0xFFFFF9C4), // Light Yellow
    Color(0xFFFFE0B2), // Light Orange
    Color(0xFFE1BEE7), // Light Purple
    Color(0xFFC8E6C9), // Light Green
    Color(0xFFFFCDD2)  // Light Red
)
