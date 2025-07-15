package com.dalima.paisawise

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomBottomBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Background Surface
        Surface(
            color = Color(0xFFFFF0DB),
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {}

        // Navigation Row
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .align(Alignment.BottomCenter)
        ) {
            BottomBarItem(icon = Icons.Default.Home, index = 0, selectedIndex, onItemSelected)
            BottomBarItem(icon = Icons.Default.Share, index = 1, selectedIndex, onItemSelected)

            Spacer(modifier = Modifier.width(50.dp)) // Space for FAB

            BottomBarItem(icon = Icons.Default.Person, index = 2, selectedIndex, onItemSelected)
            BottomBarItem(icon = Icons.Default.Person, index = 3, selectedIndex, onItemSelected)
        }

        // Center Floating Action Button
        FloatingActionButton(
            onClick = { onItemSelected(4) },
            backgroundColor = Color(0xFF4CAF50),
            contentColor = Color.White,
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-15).dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
        }
    }
}

@Composable
fun BottomBarItem(
    icon: ImageVector,
    index: Int,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    IconButton(onClick = { onItemSelected(index) }) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (index == selectedIndex) Color(0xFF4CAF50) else Color.Gray,
            modifier = Modifier.size(28.dp)
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
