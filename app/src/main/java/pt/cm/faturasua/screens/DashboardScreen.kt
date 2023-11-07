package pt.cm.faturasua.screens

import android.icu.text.DecimalFormat
import android.icu.util.Calendar
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.StateFlow
import pt.cm.faturasua.R
import pt.cm.faturasua.viewmodel.UserViewModel
import pt.cm.faturasua.components.DashboardInvoiceCard
import pt.cm.faturasua.components.InvoiceCard
import pt.cm.faturasua.data.Profile

@Preview
@Composable
fun DashboardScreen(
    userViewModel: UserViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState)
            .padding(vertical = 20.dp)
            .padding(bottom = 50.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DashboardGreeting(
            profile = userViewModel.profile
        )

        // Calculate invoice total amounts per sector
        val receiptsList = userViewModel.receiptsList.collectAsState().value

        var totalAmountGeneralExpenses = 0.00
        var totalAmountMeals = 0.00
        var totalAmountEducation = 0.00
        var totalAmountHealth = 0.00
        var totalAmountProperty = 0.00

        var pendingInvoicesCounter = 0

        receiptsList.forEach {
            when (it.category) {
                "GE" -> totalAmountGeneralExpenses += it.amount.toDouble()
                "M" -> totalAmountMeals += it.amount.toDouble()
                "E" -> totalAmountEducation += it.amount.toDouble()
                "H" -> totalAmountHealth += it.amount.toDouble()
                "P" -> totalAmountProperty += it.amount.toDouble()
            }
            if (it.status == null)
                pendingInvoicesCounter++
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.inversePrimary,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.dashboard_tax_number_display,
                    userViewModel.nif.value.toString().chunked(3).joinToString(separator = " ")
                ),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(10.dp)
            )
        }

        if (pendingInvoicesCounter > 0){
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 3.dp)
            ) {
                Text(
                    text = stringResource(R.string.dashboard_pending_invoices, pendingInvoicesCounter),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(10.dp)
                )
            }
        }

        Row (
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth().padding(top = 5.dp)
        ) {
            Text(
                text = stringResource(R.string.dashboard_total_amount_categories_label),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .padding(top = 10.dp)
            )
        }

        // Overview of invoices per sector
        DashboardInvoiceCard(
            icon = Icons.Default.List,
            color = MaterialTheme.colorScheme.tertiaryContainer,
            category = "gerais",
            title = stringResource(R.string.dashboard_category_general_expenses),
            amount = totalAmountGeneralExpenses
        )
        DashboardInvoiceCard(
            icon = Icons.Default.ShoppingCart,
            color = MaterialTheme.colorScheme.secondaryContainer,
            category = "alimentacao",
            title = stringResource(R.string.dashboard_category_meals),
            amount = totalAmountMeals
        )
        DashboardInvoiceCard(
            icon = Icons.Default.Face,
            color = MaterialTheme.colorScheme.background,
            category = "educacao",
            title = stringResource(R.string.dashboard_category_education),
            amount = totalAmountEducation
        )
        DashboardInvoiceCard(
            icon = Icons.Default.Favorite,
            color = MaterialTheme.colorScheme.errorContainer,
            category = "saude",
            title = stringResource(R.string.dashboard_category_health),
            amount = totalAmountHealth
        )
        DashboardInvoiceCard(
            icon = Icons.Default.Home,
            color = MaterialTheme.colorScheme.surface,
            category = "imoveis",
            title = stringResource(R.string.dashboard_category_property),
            amount = totalAmountProperty
        )
    }
}

@Composable
fun DashboardGreeting(
    profile : StateFlow<Profile>
){
    val profile = profile.collectAsState().value
    val hour : Int = Calendar.getInstance()[Calendar.HOUR_OF_DAY]
    val greeting : String = if (hour >= 20) {
        stringResource(R.string.dashboard_greeting_night)
    } else if (hour >= 13) {
        stringResource(R.string.dashboard_greeting_afternoon)
    } else if (hour >= 6) {
        stringResource(R.string.dashboard_greeting_morning)
    } else {
        stringResource(R.string.dashboard_greeting_bedtime)
    }
    val user : String = profile.name
    Text("$greeting, $user!", fontSize = 23.sp, fontWeight = FontWeight.Medium)
    Spacer(modifier = Modifier.size(15.dp))
    Text(stringResource(R.string.dashboard_summary_label))
    Spacer(modifier = Modifier.size(15.dp))
}

fun formatPrice(price: Number): String {
    val decimalFormat = DecimalFormat("#,##0.00")
    return decimalFormat.format(price)
}