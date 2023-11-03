package pt.cm.faturasua.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import pt.cm.faturasua.screens.formatPrice
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun InvoiceCard(
    type: String,
    number: String,
    title: String,
    amount: Number,
    date: String,
    nif: Number,
    iva: Number,
    status: Boolean?
) {

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
                        text = LocalDate.parse(date, DateTimeFormatter.BASIC_ISO_DATE).format(
                            DateTimeFormatter.ofPattern("dd/MM/yyyy")),
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
                InvoiceCardDetailInfo(
                    type = type,
                    number = number,
                    title = title,
                    amount = amount,
                    date = date,
                    nif = nif,
                    iva = iva,
                    status = status
                )
            }
        }
    }
}