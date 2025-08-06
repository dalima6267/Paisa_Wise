package com.dalima.paisawise.navigatoon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.dalima.paisawise.R

@Composable
fun CustomBottomBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Bottom Bar Background
        Surface(
            color = Color(0xFFFFF0DB),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .align(Alignment.BottomCenter)
        ) {}

        // Bottom bar icons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomBarIcon(R.drawable.mainhome, 0, selectedIndex, onItemSelected)
            BottomBarIcon(R.drawable.transaction, 1, selectedIndex, onItemSelected)

            Spacer(modifier = Modifier.width(60.dp)) // Space for FAB

            BottomBarIcon(R.drawable.budget, 2, selectedIndex, onItemSelected)
            BottomBarIcon(R.drawable.profilelast, 3, selectedIndex, onItemSelected)
        }

        // White background behind FAB (simulated notch)
        Box(
            modifier = Modifier
                .size(70.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-15).dp)
                .background(Color.White, shape = CircleShape)
        )
        FloatingActionButton(
            onClick = { onItemSelected(4) },
            backgroundColor = Color.Transparent,
            elevation = FloatingActionButtonDefaults.elevation(6.dp),
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-15).dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.add),
                contentDescription = "Add",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}


@Composable
fun BottomBarIcon(
    iconResId: Int,
    index: Int,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    IconButton(onClick = { onItemSelected(index) }) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = if (index == selectedIndex) Color(0xFF4CAF50) else Color.Gray,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomBottomBarPreview() {
    val selectedIndex = remember { mutableStateOf(0) }

    CustomBottomBar(
        selectedIndex = selectedIndex.value,
        onItemSelected = { selectedIndex.value = it }
    )
}
