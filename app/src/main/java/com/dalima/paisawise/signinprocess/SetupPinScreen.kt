package com.dalima.paisawise.signinprocess

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

import com.dalima.paisawise.ui.theme.LightGreen
import androidx.compose.material3.Icon
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dalima.paisawise.Enum.Screen
import com.dalima.paisawise.PinStorage


@Composable
fun SetupPinScreen(navController: NavController) {
    val pinLength = 4
    var pin by remember { mutableStateOf("") }

    val context = LocalContext.current
    val savedPin = remember { PinStorage.getSavedPin(context) }
    val isSetupMode = savedPin == null
    val numbers = listOf(
        "1", "2", "3",
        "4", "5", "6",
        "7", "8", "9",
        "", "0", ""
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGreen),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        // Title Text
        Text(
            text = "Let's  setup your PIN",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Dots
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pinLength) { index ->
                val isFilled = index < pin.length
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .background(
                            color = if (isFilled) Color.White else Color.Transparent,
                            shape = CircleShape
                        )
                        .border(
                            width = 2.dp,
                            color = if (isFilled) Color.White else Color.White.copy(alpha = 0.4f),
                            shape = CircleShape
                        )
                )
            }
        }
        Spacer(modifier = Modifier.height(290.dp))
        // Keypad
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp), // control keypad area height
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            userScrollEnabled = false
        ) {
            items(numbers.size) { index ->
                val label = numbers[index]
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .aspectRatio(2f)
                        .clickable {
                            if (label.isNotEmpty() && label != "←") {
                                if (pin.length < pinLength) pin += label
                            }
                        }
                ) {
                    when (label) {
                        "" -> {
                            if (index == numbers.lastIndex) {
                                FloatingActionButton(
                                    onClick = {
                                        when {
                                            pin.isEmpty() -> {
                                                Toast.makeText(context, "Pin Required!", Toast.LENGTH_SHORT).show()
                                            }
                                            pin.length < pinLength -> {
                                                Toast.makeText(context, "Pin Incomplete!", Toast.LENGTH_SHORT).show()
                                            }
                                            else -> {
                                                if (isSetupMode) {
                                                    // Save PIN for first time
                                                    PinStorage.savePin(context, pin)
                                                    navController.navigate(Screen.ExpenseCategory.name) {
                                                        popUpTo(Screen.SetupPin.name) { inclusive = true }
                                                    }
                                                } else {
                                                    // Validate PIN
                                                    if (pin == savedPin) {
                                                        navController.navigate(Screen.ExpenseCategory.name) {
                                                            popUpTo(Screen.SetupPin.name) { inclusive = true }
                                                        }
                                                    } else {
                                                        Toast.makeText(context, "Incorrect Pin!", Toast.LENGTH_SHORT).show()
                                                        pin = "" // Clear entered PIN
                                                    }
                                                }
                                            }
                                        }
                                    },
                                    backgroundColor = Color(0xFFA7FFEB),
                                    contentColor = Color.Black,
                                    modifier = Modifier
                                        .width(50.dp)
                                        .height(35.dp)
                                ) {
                                    Icon(Icons.Default.ArrowForward, contentDescription = "Next")
                                }
                            } else {
                                Spacer(modifier = Modifier)
                            }
                        }

                        else -> Text(
                            text = label,
                            color = Color.White,
                            fontSize = 34.sp
                        )
                    }
                }
            }

        }

        // Submit Button
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 16.dp, bottom = 20.dp, end = 16.dp),
//            contentAlignment = Alignment.BottomEnd
//        ) {
//            FloatingActionButton(
//                onClick = {
//                    // Handle PIN continue/navigation
//                },
//                containerColor = Color(0xFFA7FFEB),
//                contentColor = Color.Black,
//                modifier = Modifier
//                    .width(50.dp)
//                    .height(35.dp)
//            ) {
//                Icon(Icons.Default.ArrowForward, contentDescription = "Next")
//            }
//        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SetupPinScreenPreview() {
    SetupPinScreen(
        navController = rememberNavController()
    )
}
//val startDestination = if (PinStorage.hasPin(context)) {
//    Screen.SetupPin.name // PIN check screen
//} else {
//    Screen.SetupPin.name // same screen, but saving logic will run
//}
