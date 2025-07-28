package com.dalima.paisawise.navigatoon

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun TopBarWithMonthPicker(
    modifier: Modifier = Modifier,
    onNotificationClick: () -> Unit = {},
    onMonthSelected: (String) -> Unit = {}
) {
    val currentMonth = remember {
        SimpleDateFormat("MMMM", Locale.getDefault()).format(Date())
    }
    var expanded by remember { mutableStateOf(false) }
    var selectedMonth by rememberSaveable { mutableStateOf(currentMonth) }

    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    TopAppBar(
        backgroundColor = Color(0xFFF1F9F5),
        elevation = 0.dp,
        modifier = modifier
    ) {
        // Left: Logo
        Row(
            modifier = Modifier
                .padding(start = 16.dp)
                .size(36.dp)
                .clip(CircleShape)
                .border(1.dp, Color(0xFF47A671), CircleShape),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Spli",
                color = Color(0xFF47A671),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Center: Month Dropdown
        Box {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .clickable { expanded = true }
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Arrow",
                    tint = Color(0xFF47A671)
                )
                Text(text = selectedMonth, color = Color.Black)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                months.forEach { month ->
                    DropdownMenuItem(onClick = {
                        selectedMonth = month
                        expanded = false
                        onMonthSelected(month)
                    }) {
                        Text(text = month)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Right: Notification Icon
        IconButton(onClick = onNotificationClick) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Color(0xFF47A671)
            )
        }
    }
}
