package pt.cm.faturasua.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.cm.faturasua.R
import pt.cm.faturasua.components.InvoiceCard
import pt.cm.faturasua.utils.FirebaseUtil
import pt.cm.faturasua.viewmodel.UserViewModel


@Composable
fun HistoryScreen(
    userViewModel: UserViewModel = viewModel(),
    firebaseUtil: FirebaseUtil
){
    val scrollState = rememberScrollState()
    val receiptsList = userViewModel.receiptsList.collectAsState().value
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState)
            .padding(vertical = 10.dp)
            .padding(bottom = 60.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Search bar
        TextField(
            value = searchQuery,
            singleLine = true,
            trailingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "") },
            onValueChange = { newValue -> searchQuery = newValue },
            label = { Text(stringResource(R.string.history_search_bar_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        )

        var sliderPosition by remember { mutableStateOf(0f..2000f) }

        // Range slider
        Box(
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)
        ) {
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column (
                    modifier = Modifier.weight(1.6f)
                ) {
                    RangeSlider(
                        value = sliderPosition,
                        onValueChange = { range -> sliderPosition = range },
                        valueRange = 0f..2000f
                    )
                    Row (
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "Min: ${sliderPosition.start.toInt()}€",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Left,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "Max: ${sliderPosition.endInclusive.toInt()}€",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.weight(1f)
                        )
                    }

                }
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(0.4f)
                        .clickable {
                            searchQuery = ""
                            sliderPosition = 0f..2000f
                        },
                ){
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "", tint = MaterialTheme.colorScheme.error)
                    Text(text = stringResource(R.string.history_clear_filters),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        // Invoice list
        if (receiptsList.isEmpty()) {
            Text(
                text = "No invoices have been added yet!\n\nStart by adding one by clicking on the ＋ icon on the floating button.",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(20.dp)
            )
        } else {
            // Filter the receipts list based on the search query
            val filteredReceiptsList = receiptsList.filter { receipt ->
                val amount = receipt.amount.toDouble()
                (
                        (receipt.id.contains(searchQuery, ignoreCase = true) ||
                        receipt.title.contains(searchQuery, ignoreCase = true) ||
                        receipt.businessNIF.contains(searchQuery, ignoreCase = true) ||
                        receipt.amount.contains(searchQuery)
                        )
                        && (amount >= sliderPosition.start) && (amount <= sliderPosition.endInclusive)
                )
            }

            filteredReceiptsList.forEach {
                InvoiceCard(
                    firebaseUtil = firebaseUtil,
                    type = it.type,
                    category = it.category,
                    timestamp = it.timestamp,
                    number = it.id,
                    title = it.title,
                    amount = it.amount.toDouble(),
                    date = it.date,
                    nif = it.businessNIF.toInt(),
                    iva = it.iva.toDouble(),
                    status = it.status,
                    user = ""
                )
            }
        }

/*        InvoiceCard(type = "FS", number = "FS 0603065224354619/2349", title = "COMPRA MCDONALDS", amount = 102, date = "20220214", nif = 509441130, iva = 1.66, status = null)
        InvoiceCard(type = "FT", number = "FT 11435234619/015229", title = "PAGAMENTO DE SERVICOS", amount = 1235.99, date = "20220221", nif = 509441130, iva = 1.66, status = true)
        InvoiceCard(type = "ND", number = "ND 23406234321010619/222", title = "LEVANTAMENTO MULTIBANCO", amount = 51.5, date = "20220222", nif = 509441130, iva = 1.66, status = true)
        InvoiceCard(type = "NC", number = "NC 123349/015229", title = "COMPRA QUIOSQUE", amount = 99.99, date = "20220223", nif = 509441130, iva = 1.66, status = true)
        InvoiceCard(type = "RC", number = "RC 732340010619/123", title = "COMPRA RAMONA", amount = 4.2, date = "20220224", nif = 509441130, iva = 1.66, status = true)
        InvoiceCard(type = "OR", number = "OR 123652201010619/019", title = "COMPRA GLICINIAS PLAZA", amount = 4.2, date = "20230227", nif = 509441130, iva = 1.66, status = false)
        InvoiceCard(type = "FS", number = "FS 2142/210", title = "COMPRA TEXTO EXTREMAMENTE LARGO OVERFLOW", amount = 111.69, date = "20230720", nif = 509441130, iva = 1.66, status = true)
   */
    }
}
