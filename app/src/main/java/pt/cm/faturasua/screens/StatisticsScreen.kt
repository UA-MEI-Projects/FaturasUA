package pt.cm.faturasua.screens

import android.widget.Space
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jaikeerthick.composable_graphs.color.Gradient2
import com.jaikeerthick.composable_graphs.color.Gradient3
import com.jaikeerthick.composable_graphs.color.GraphAccent2
import com.jaikeerthick.composable_graphs.color.LinearGraphColors
import com.jaikeerthick.composable_graphs.color.PointHighlight2
import com.jaikeerthick.composable_graphs.composables.BarGraph
import com.jaikeerthick.composable_graphs.composables.LineGraph
import com.jaikeerthick.composable_graphs.data.GraphData
import com.jaikeerthick.composable_graphs.style.LineGraphStyle
import com.jaikeerthick.composable_graphs.style.LinearGraphVisibility
import pt.cm.faturasua.utils.StatsUtil
import pt.cm.faturasua.viewmodel.UserViewModel

@Composable
fun StatisticsScreen(
    userViewModel: UserViewModel = viewModel()
){
    val receiptsList = userViewModel.receiptsList.collectAsState().value
    var scrollState = rememberScrollState()
    var data = StatsUtil.scanChartData(receiptsList)
    var totalExpenses = StatsUtil.scanTotalValue(receiptsList)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.verticalScroll(scrollState, enabled = true)
            .padding(30.dp)
    ) {
        Text("Número de Recibos Inseridos")
        Text(receiptsList.size.toString())
        Spacer(Modifier.size(40.dp))
        Text("Atividade do Utilizador")
        Spacer(Modifier.size(40.dp))


        val keys = data.map { GraphData.String(it.key) }
        val values = data.values.toList()


        if(receiptsList.size < 4){
            Text("Não existe registos suficientes para mostrar informação.")
        }
        else{
            Text("Gastos totais:")
            Text(totalExpenses.toString())
            LineGraph(
                xAxisData = keys,
                yAxisData = values,
                header = { Text("Registos por data")},
                style = LineGraphStyle(
                    visibility = LinearGraphVisibility(
                        isHeaderVisible = true,
                        isXAxisLabelVisible = false,
                        isYAxisLabelVisible = true,
                        isCrossHairVisible = true,
                        isGridVisible = true,
                    ),
                    colors = LinearGraphColors(
                        lineColor = GraphAccent2,
                        pointColor = GraphAccent2,
                        clickHighlightColor = PointHighlight2,
                        fillGradient = Brush.verticalGradient(
                            listOf(Gradient3, Gradient2)
                        )
                    )
                )
            )
        }
    }
}

@Preview
@Composable
fun StatsPrev(){
    StatisticsScreen()
}

