import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.WeatherInfo

@Composable
fun WeatherChart(weatherForecast: List<WeatherInfo>) {
    // Crea i punti del grafico
    val entries = weatherForecast.mapIndexed { index, info ->
        Entry(index.toFloat(), info.temperature.toFloat())
    }

    // Etichette dell'asse X → orari (es. "10:00", "11:00", ecc.)
    val timeLabels = weatherForecast.map { it.time }

    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                val dataSet = LineDataSet(entries, "Temperatura").apply {
                    color = Color.BLUE
                    setDrawValues(false)
                    setDrawCircles(true)
                    circleRadius = 4f
                    setCircleColor(Color.BLUE)
                    lineWidth = 2f
                    mode = LineDataSet.Mode.CUBIC_BEZIER // curva più fluida
                }

                data = LineData(dataSet)

                // Configurazione asse X
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    valueFormatter = IndexAxisValueFormatter(timeLabels)
                    granularity = 1f
                    setDrawGridLines(false)
                    textColor = Color.BLACK
                }

                // Configurazione asse Y
                axisLeft.textColor = Color.BLACK
                axisRight.isEnabled = false

                description.isEnabled = false
                legend.isEnabled = false
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(8.dp)
    )
}
