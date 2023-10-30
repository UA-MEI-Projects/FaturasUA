package pt.cm.faturasua.screens

import android.icu.text.DecimalFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun HistoryScreen(){
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "History",
            fontSize = 30.sp,
            color = Color.Black,
            textAlign = TextAlign.Left
        )

        InvoiceCard(id = 1, number = "FS 06030652201010619/015229", title = "COMPRA MCDONALDS", amount = 102, date = "20220214", nif = 509441130, iva = 1.66)
        InvoiceCard(id = 2, number = "FS 06030652201010619/015229", title = "PAGAMENTO DE SERVICOS", amount = 1235.99, date = "20220221", nif = 509441130, iva = 1.66)
        InvoiceCard(id = 3, number = "FS 06030652201010619/015229", title = "LEVANTAMENTO MULTIBANCO", amount = 51.5, date = "20220222", nif = 509441130, iva = 1.66)
        InvoiceCard(id = 4, number = "FS 06030652201010619/015229", title = "COMPRA QUIOSQUE", amount = 99.99, date = "20220223", nif = 509441130, iva = 1.66)
        InvoiceCard(id = 5, number = "FS 06030652201010619/015229", title = "COMPRA RAMONA", amount = 4.2, date = "20220224", nif = 509441130, iva = 1.66)
        InvoiceCard(id = 6, number = "FS 06030652201010619/015229", title = "COMPRA GLICINIAS PLAZA", amount = 4.2, date = "20230230", nif = 509441130, iva = 1.66)
        InvoiceCard(id = 7, number = "FS 06030652201010619/015229", title = "COMPRA TEXTO EXTREMAMENTE LARGO OVERFLOW", amount = 111.69, date = "20230720", nif = 509441130, iva = 1.66)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceCard(id: Int, number: String, title: String, amount: Number, date: String, nif: Number, iva: Number) {

    var expanded by remember { mutableStateOf (false) }

    Card(
        onClick = { expanded = !expanded },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column {
                    var year : String = date.substring(startIndex = 0, endIndex = 4)
                    var month : String = date.substring(startIndex = 4, endIndex = 6)
                    var day : String = date.substring(startIndex = 6, endIndex = 8)

                    Text(
                        text = "$day/$month/$year",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 5.dp),
                        color = MaterialTheme.colorScheme.tertiary,
                        textAlign = TextAlign.Left,
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                        textAlign = TextAlign.Left,
                    )
                }
                Text(
                    text = "${formatPrice(amount)}€",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(5.dp),
                    textAlign = TextAlign.Right,
                )
            }

            if (expanded) {
                Text(
                    text = "Identifier: #$id",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 2.dp)
                )
                Text(
                    text = "Invoice number: $number",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
                Text(
                    text = "NIF: ${nif.toString().chunked(3).joinToString(separator = " ")}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
                // TODO: Perceber porque é que a operação de diferença dá erro se não forem inteiros
                var value : Number = (amount.toInt() - iva.toInt())
                Text(
                    text = "Valor líquido (sem IVA): ${formatPrice(value)}€",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
                Text(
                    text = "Valor IVA: ${formatPrice(iva)}€",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
                Text(
                    text = "Valor total pago: ${formatPrice(amount)}€",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
            }
        }
    }
}

fun formatPrice(price: Number): String {
    val decimalFormat = DecimalFormat("#,##0.00")
    return decimalFormat.format(price)
}
