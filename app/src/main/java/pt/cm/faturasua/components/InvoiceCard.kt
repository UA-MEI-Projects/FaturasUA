package pt.cm.faturasua.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import pt.cm.faturasua.R
import pt.cm.faturasua.screens.formatPrice
import pt.cm.faturasua.utils.FirebaseUtil
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun InvoiceCard(
    firebaseUtil : FirebaseUtil,
    adminMode : Boolean = false,
    type: String,
    category : String,
    timestamp: String,
    number: String,
    title: String,
    amount: Number,
    date: String,
    nif: Number,
    iva: Number,
    user: String,
    status: Boolean?
) {

    var expanded by remember { mutableStateOf (false) }
    var liveStatus by remember { mutableStateOf(status) }

    val color = when (liveStatus) {
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
                        text = title.uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                            .width(220.dp),
                        textAlign = TextAlign.Left,
                        color = MaterialTheme.colorScheme.tertiary,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
                Column (
                    horizontalAlignment = Alignment.End
                ) {
                    Row(){
                        Text(
                            text = when (liveStatus) {
                                true -> stringResource(R.string.invoice_status_authorised).uppercase()
                                false -> stringResource(R.string.invoice_status_rejected).uppercase()
                                else -> stringResource(R.string.invoice_status_pending).uppercase()
                            },
                            modifier = Modifier.padding(horizontal = 5.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = when (liveStatus) {
                                true -> MaterialTheme.colorScheme.primary
                                false -> MaterialTheme.colorScheme.error
                                else -> MaterialTheme.colorScheme.tertiary
                            },
                            textAlign = TextAlign.Right,
                        )
                        Icon(
                            imageVector = when (liveStatus) {
                                true -> Icons.Rounded.Check
                                false -> Icons.Rounded.Close
                                else -> Icons.Rounded.Warning
                            },
                            modifier = Modifier.size(15.dp),
                            contentDescription = "",
                            tint = when (liveStatus) {
                                true -> MaterialTheme.colorScheme.primary
                                false -> MaterialTheme.colorScheme.error
                                else -> MaterialTheme.colorScheme.tertiary
                            },
                        )
                    }
                    Text(
                        text = "${formatPrice(amount)}€",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                        textAlign = TextAlign.Right,
                    )
                }
            }

            if (adminMode && liveStatus == null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                                firebaseUtil.changeReceiptStatus(number,user, true)
                                liveStatus = true
                                  },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .padding(3.dp)
                    ) {
                        Icon(
                            Icons.Rounded.Check,
                            contentDescription = stringResource(R.string.invoice_button_authorise)
                        )
                        Text(text = " " + stringResource(R.string.invoice_button_authorise))
                    }
                    Button(
                        onClick = {
                            firebaseUtil.changeReceiptStatus(number,user, false)
                            liveStatus = false
                                  },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .padding(3.dp)
                    ) {
                        Icon(
                            Icons.Rounded.Close,
                            contentDescription = stringResource(R.string.invoice_button_reject)
                        )
                        Text(text = " " + stringResource(R.string.invoice_button_reject))
                    }
                }
            }

            if (expanded) {
                InvoiceCardDetailInfo(
                    type = type,
                    category = category,
                    timestamp = timestamp,
                    number = number,
                    title = title,
                    amount = amount,
                    date = date,
                    nif = nif,
                    iva = iva,
                    status = liveStatus
                )
            }
        }
    }
}