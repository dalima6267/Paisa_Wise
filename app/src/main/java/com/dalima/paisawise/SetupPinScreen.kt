package com.dalima.paisawise

import android.R.attr.label
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.*
import com.dalima.paisawise.ui.theme.LightGreen
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon


@Composable
fun SetupPinScreen() {
    val pinLength = 4
    var pin by remember { mutableStateOf("") }

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
                                // Show FAB at last position
                                FloatingActionButton(
                                    onClick = {
                                        // Handle click
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
    SetupPinScreen()
}