package pt.cm.faturasua.screens

import android.icu.text.DecimalFormat
import android.icu.util.Calendar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun DashboardScreen() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Dashboard",
            fontSize = 30.sp,
            color = Color.Black
        )
        DashboardGreeting()

        Card(
            //onClick = { true },
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.inversePrimary,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            // TODO: Get the tax number (NIF) dynamically from the DB
            val taxNumber : Number = 248239230
            Text(
                text = "Your tax number: ${taxNumber.toString().chunked(3).joinToString(separator = " ")}",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(10.dp)
            )
        }

        // Overview of invoices per sector
        InvoiceCard(icon = Icons.Rounded.Home, color = MaterialTheme.colorScheme.tertiaryContainer, category = "gerais", title = "Despesas Gerais", amount = 6251.11)
        InvoiceCard(icon = Icons.Rounded.ShoppingCart, color = MaterialTheme.colorScheme.secondaryContainer, category = "alimentacao", title = "Alimentação", amount = 800.34)
        InvoiceCard(icon = Icons.Rounded.ShoppingCart, color = MaterialTheme.colorScheme.background, category = "educacao", title = "Educação", amount = 1486.09)
        InvoiceCard(icon = Icons.Rounded.ShoppingCart, color = MaterialTheme.colorScheme.errorContainer, category = "saude", title = "Saúde", amount = 67.67)
        InvoiceCard(icon = Icons.Rounded.ShoppingCart, color = MaterialTheme.colorScheme.surface, category = "imoveis", title = "Imóveis", amount = 342.24)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceCard(icon: ImageVector, color: Color, category: String, title: String, amount: Number) {
    Card(
        //onClick = { true },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = color,
        ),
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
                Icon(
                    icon,
                    contentDescription = "Icon",
                    modifier = Modifier.padding(5.dp),
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 5.dp)
                        .weight(1f)
                )
                Text(
                    text = "${formatPrice(amount)}€",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(5.dp),
                    textAlign = TextAlign.Right,
                )
            }
        }
    }
}

@Composable
fun DashboardGreeting(){
    val hour : Int = Calendar.getInstance()[Calendar.HOUR_OF_DAY]
    val greeting : String = if (hour >= 20) {
        "Good night,"
    } else if (hour >= 13) {
        "Good afternoon,"
    } else if (hour >= 6) {
        "Good morning,"
    } else {
        "Go to sleep,"
    }
    val user : String = "Username" // TODO: Fetch username from Google auth
    Text("$greeting $user!")
}

// TODO: Get all invoices from that sector on the database and sum the amount of all of them
fun totalInvoicesPerSector(sector: String) : Number {
    return 0
}

fun formatPrice(price: Number): String {
    val decimalFormat = DecimalFormat("#,##0.00")
    return decimalFormat.format(price)
}