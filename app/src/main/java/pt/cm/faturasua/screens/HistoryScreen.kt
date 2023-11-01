package pt.cm.faturasua.screens

import android.icu.text.DecimalFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

        InvoiceCard(type = "FS", number = "FS 0603065224354619/2349", title = "COMPRA MCDONALDS", amount = 102, date = "20220214", nif = 509441130, iva = 1.66, status = null)
        InvoiceCard(type = "FT", number = "FT 11435234619/015229", title = "PAGAMENTO DE SERVICOS", amount = 1235.99, date = "20220221", nif = 509441130, iva = 1.66, status = true)
        InvoiceCard(type = "ND", number = "ND 23406234321010619/222", title = "LEVANTAMENTO MULTIBANCO", amount = 51.5, date = "20220222", nif = 509441130, iva = 1.66, status = true)
        InvoiceCard(type = "NC", number = "NC 123349/015229", title = "COMPRA QUIOSQUE", amount = 99.99, date = "20220223", nif = 509441130, iva = 1.66, status = true)
        InvoiceCard(type = "RC", number = "RC 732340010619/123", title = "COMPRA RAMONA", amount = 4.2, date = "20220224", nif = 509441130, iva = 1.66, status = true)
        InvoiceCard(type = "OR", number = "OR 123652201010619/019", title = "COMPRA GLICINIAS PLAZA", amount = 4.2, date = "20230227", nif = 509441130, iva = 1.66, status = false)
        InvoiceCard(type = "FS", number = "FS 2142/210", title = "COMPRA TEXTO EXTREMAMENTE LARGO OVERFLOW", amount = 111.69, date = "20230720", nif = 509441130, iva = 1.66, status = true)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceCard(type: String, number: String, title: String, amount: Number, date: String, nif: Number, iva: Number, status : Boolean?) {

    var expanded by remember { mutableStateOf (false) }

    val color = when (status) {
        true -> MaterialTheme.colorScheme.surfaceTint
        false -> MaterialTheme.colorScheme.errorContainer
        else -> MaterialTheme.colorScheme.tertiaryContainer
    }

    Card(
        onClick = { expanded = !expanded },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = color
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
                    Text(
                        text = LocalDate.parse(date, DateTimeFormatter.BASIC_ISO_DATE).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 5.dp),
                        textAlign = TextAlign.Left,
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                            .width(230.dp),
                        textAlign = TextAlign.Left,
                        color = MaterialTheme.colorScheme.tertiary,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
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
                    text = "Status: ${if (status == true) "Approved ✅" else if (status == false) "Rejected ❌" else "Pending 🕑"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 2.dp)
                )
                // TODO: Hardcoded type (for now)
                val type = "FS"
                var formatType : String = type
                when (type) {
                    "OR" -> formatType = "Orçamento"
                    "FP" -> formatType = "Fatura Pró-forma"
                    "FT" -> formatType = "Fatura"
                    "FS" -> formatType = "Fatura Simplificada"
                    "FR" -> formatType = "Fatura Recibo"
                    "ND" -> formatType = "Nota de Débito"
                    "NC" -> formatType = "Nota de Crédito"
                    "FCA" -> formatType = "Fatura de Auto-faturação"
                    "RC" -> formatType = "Recibo"
                }
                Text(
                    text = "Invoice type: $formatType",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
                Text(
                    text = "Invoice number: $number",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
                Text(
                    text = "NIF: ${nif.toString().chunked(3).joinToString(separator = " ")}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
                var value : Number = (amount.toDouble() -  iva.toDouble())
                Text(
                    text = "Valor líquido (sem IVA): ${formatPrice(value)}€",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
                Text(
                    text = "Valor IVA: ${formatPrice(iva)}€",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
                Text(
                    text = "Valor total pago: ${formatPrice(amount)}€",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
                // TODO: Hardcoded timestamp (for now)
                val formatTimestamp = LocalDateTime.parse("2023-10-31T19:06:14.963943308", DateTimeFormatter.ISO_DATE_TIME).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                Text(
                    text = "Digitalizada em: $formatTimestamp",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
            }
        }
    }
}
