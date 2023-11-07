package pt.cm.faturasua.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.cm.faturasua.R
import pt.cm.faturasua.screens.formatPrice
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun InvoiceCardDetailInfo(type: String, category: String, timestamp: String, number: String, title: String, amount: Number, date: String, nif: Number, iva: Number, status : Boolean?) {
    val categoryType : String = when (category) {
        "GE" -> stringResource(R.string.dashboard_category_general_expenses)
        "M" -> stringResource(R.string.dashboard_category_meals)
        "E" -> stringResource(R.string.dashboard_category_education)
        "H" -> stringResource(R.string.dashboard_category_health)
        "P" -> stringResource(R.string.dashboard_category_property)
        else -> category
    }
    Text(
        text = stringResource(R.string.invoice_category, categoryType),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
    )
    val formatType : String = when (type) {
        "OR" -> "Orçamento"
        "FP" -> "Fatura Pró-Forma"
        "FT" -> "Fatura"
        "FS" -> "Fatura Simplificada"
        "FR" -> "Fatura-Recibo"
        "ND" -> "Nota de Débito"
        "NC" -> "Nota de Crédito"
        "NE" -> "Nota de Encomenda"
        "FCA" -> "Fatura de Auto-Faturação"
        "RG" -> "Recibo"
        "RC" -> "Recibo IVA de Caixa"
        "CM" -> "Consultas de Mesa"
        "GR" -> "Guia de Remessa"
        "GT" -> "Guia de Transporte"
        "GD" -> "Guia ou Nota de Devolução"
        else -> type
    }
    Text(
        text = stringResource(R.string.invoice_type, formatType),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
    )
    Text(
        text = stringResource(R.string.invoice_number, number),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
    )
    /*Text(
        text = stringResource(R.string.invoice_entity_name, true), // TODO: obter da API
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
    )*/
    Text(
        text = stringResource(
            R.string.invoice_entity_nif,
            nif.toString().chunked(3).joinToString(separator = " ")
        ),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
    )
    /*
    Text(
        text = stringResource(R.string.invoice_tax_address),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
    )
    Text(text = "[TODO: obter da API e mostrar mapa aqui]", modifier = Modifier.padding(vertical = 15.dp))
    */
    val value : Number = (amount.toDouble() -  iva.toDouble())
    Text(
        text = stringResource(R.string.invoice_net_price, formatPrice(value)),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
    )
    Text(
        text = stringResource(R.string.invoice_vat_price, formatPrice(iva)),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
    )
    Text(
        text = stringResource(R.string.invoice_total_amount, formatPrice(amount)),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
    )
    val formatTimestamp = LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME).format(
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
    Text(
        text = stringResource(R.string.invoice_scan_date, formatTimestamp),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
    )
}
