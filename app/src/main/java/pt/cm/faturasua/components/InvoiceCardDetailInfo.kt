package pt.cm.faturasua.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.cm.faturasua.screens.formatPrice
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun InvoiceCardDetailInfo(type: String, number: String, title: String, amount: Number, date: String, nif: Number, iva: Number, status : Boolean?) {
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
    val formatTimestamp = LocalDateTime.parse("2023-10-31T19:06:14.963943308", DateTimeFormatter.ISO_DATE_TIME).format(
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
    Text(
        text = "Digitalizada em: $formatTimestamp",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
    )
}
