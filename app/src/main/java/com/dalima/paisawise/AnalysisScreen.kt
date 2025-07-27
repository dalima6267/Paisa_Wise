package com.dalima.paisawise
import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import androidx.compose.ui.viewinterop.AndroidView
import com.dalima.paisawise.ui.theme.ButtonGreen
import com.dalima.paisawise.ui.theme.Lightred

@Composable
fun AnalysisScreen(
    analysisSummary: String,
    expensesByType: Map<String, Float>,
    onDownloadClick: () -> Unit,
    onPdfGenerateClick: () -> Unit
) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            Text("Expense Analysis Chart", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(14.dp))

            AndroidView(factory = { context ->
                PieChart(context).apply {
                    val entries = expensesByType.map { PieEntry(it.value, it.key) }
                    val dataSet = PieDataSet(entries, "Expense Types")
                    dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
                    dataSet.sliceSpace = 2f

                    this.data = PieData(dataSet)
                    this.description.isEnabled = false
                    this.legend.isEnabled = true
                    this.invalidate()
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .height(250.dp))

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Analysis Summary", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Button(onClick = onDownloadClick, colors = ButtonDefaults.buttonColors(ButtonGreen)) {
                    Text("Download", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = analysisSummary,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ComposeColor.LightGray, RoundedCornerShape(8.dp))
                    .padding(12.dp),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(214.dp))

            Button(
                onClick = onPdfGenerateClick,
                colors = ButtonDefaults.buttonColors(Lightred),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Get this report in PDF", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
@Composable
@Preview(showBackground = true)
fun SampleChartUsage() {
    val sampleData = mapOf(
        "Food" to 4500f,
        "Travel" to 2000f,
        "Entertainment" to 1500f,
        "Bills" to 1000f,
        "Essential" to 3500f,
        "Shopping" to 1000f,
        "School" to 1500f,
        "Bills" to 1000f
    )
    val summary = "This month, food made up the largest share of your expenses."

    AnalysisScreen(
        analysisSummary = summary,
        expensesByType = sampleData,
        onDownloadClick = { /* TODO */ },
        onPdfGenerateClick = { /* TODO */ }
    )
}


