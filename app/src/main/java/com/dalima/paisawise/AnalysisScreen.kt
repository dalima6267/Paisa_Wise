package com.dalima.paisawise

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import androidx.compose.ui.viewinterop.AndroidView
import com.dalima.paisawise.ui.theme.ButtonGreen

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AnalysisScreen(
    totalExpense: Double,
    expensesByType: Map<String, Double>,
    onGenerateReportClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Expense Analysis Chart",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            // 🔹 Dropdown menu
            var expanded by remember { mutableStateOf(false) }
            var selectedOption by remember { mutableStateOf("Monthly") }
            val options = listOf("Daily", "Weekly", "Monthly", "Yearly")

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedOption,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .width(120.dp)
                        .height(48.dp),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(onClick = {
                            selectedOption = option
                            expanded = false
                        }) {
                            Text(option)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        AndroidView(
            factory = { context ->
                PieChart(context).apply {
                    description.isEnabled = false
                    setUsePercentValues(false)
                    setDrawEntryLabels(false)
                    setHoleColor(Color.WHITE)
                    setTransparentCircleAlpha(0)
                    legend.isEnabled = false
                }
            },
            update = { chart ->
                chart.centerText = "Total Expense: ₹${"%.2f".format(totalExpense)}"
                chart.setCenterTextSize(18f)
                chart.setCenterTextColor(Color.BLACK)
                if (expensesByType.isEmpty()) {
                    chart.clear()
                    chart.invalidate()
                } else {
                    val entries = expensesByType.map { (category, total) ->
                        PieEntry(total.toFloat(), category)
                    }

                    val categoryColors = listOf(
                        Color.rgb(244, 67, 54),
                        Color.rgb(33, 150, 243),
                        Color.rgb(76, 175, 80),
                        Color.rgb(255, 193, 7),
                        Color.rgb(156, 39, 176),
                        Color.rgb(255, 87, 34),
                        Color.rgb(63, 81, 181),
                        Color.rgb(0, 150, 136),
                        Color.rgb(205, 220, 57),
                        Color.rgb(121, 85, 72),
                        Color.rgb(96, 125, 139),
                        Color.rgb(233, 30, 99),
                        Color.rgb(0, 188, 212)
                    )

                    val dataSet = PieDataSet(entries, "").apply {
                        colors = entries.mapIndexed { index, _ ->
                            categoryColors[index % categoryColors.size]
                        }.toMutableList()
                        sliceSpace = 1f
                        setDrawValues(false)
                    }

                    chart.data = PieData(dataSet).apply { setDrawValues(false) }
                    chart.notifyDataSetChanged()
                    chart.invalidate()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .padding(top = 10.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ComposeColor(0xFFDFEBEA))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(ButtonGreen),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                ) {
                    Text("Categorical", color = ComposeColor.White)
                }
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(ComposeColor(0xFFDFF0D8)), // Light green
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                ) {
                    Text("Categorical", color = ComposeColor.Black)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // 🔹 Generate Report Button
        Button(
            onClick = onGenerateReportClick,
            colors = ButtonDefaults.buttonColors(ButtonGreen),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 8.dp)
                .height(40.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "Generate Report",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = ComposeColor.White
            )
        }
    }
}
