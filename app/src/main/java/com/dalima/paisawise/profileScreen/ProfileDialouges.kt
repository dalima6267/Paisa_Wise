package com.dalima.paisawise.profileScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ExportBottomSheet(
    onDismiss: () -> Unit,
    onExport: (String) -> Unit
) {
    val exportOptions = listOf("CSV", "PDF", "Excel")
    var selectedOption by remember { mutableStateOf("CSV") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Export Data", style = MaterialTheme.typography.h6)

        Spacer(modifier = Modifier.height(8.dp))

        exportOptions.forEach { option ->
           Row(
               modifier = Modifier
                   .fillMaxWidth()
                   .clickable { selectedOption = option }
                   .padding(vertical = 8.dp),
               verticalAlignment = Alignment.CenterVertically
           ){
               RadioButton(
                   selected = (option == selectedOption),
                   onClick = { selectedOption = option }
               )
               Text(option, modifier = Modifier.padding(start = 8.dp))

           }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onExport(selectedOption) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFE57373)
            )
        ) {
            Text("Export")
        }
    }
}