package pt.cm.faturasua.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.get
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import pt.cm.faturasua.components.InvoiceCard
import pt.cm.faturasua.utils.FirebaseUtil
import pt.cm.faturasua.viewmodel.UserViewModel

@Preview
@Composable
fun HistoryScreen(){
    val scrollState = rememberScrollState()
    val firebaseUtil = koinInject<FirebaseUtil>()
    val userViewModel = koinViewModel<UserViewModel>()
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

        userViewModel.receiptsList.value?.forEach {
            InvoiceCard(
                type = it.type,
                number = it.id,
                title = it.title,
                amount = it.amount.toDouble(),
                date = it.date,
                nif = it.businessNIF.toInt(),
                iva = it.iva.toDouble(),
                status = it.status
            )
        }

        InvoiceCard(type = "FS", number = "FS 0603065224354619/2349", title = "COMPRA MCDONALDS", amount = 102, date = "20220214", nif = 509441130, iva = 1.66, status = null)
        InvoiceCard(type = "FT", number = "FT 11435234619/015229", title = "PAGAMENTO DE SERVICOS", amount = 1235.99, date = "20220221", nif = 509441130, iva = 1.66, status = true)
        InvoiceCard(type = "ND", number = "ND 23406234321010619/222", title = "LEVANTAMENTO MULTIBANCO", amount = 51.5, date = "20220222", nif = 509441130, iva = 1.66, status = true)
        InvoiceCard(type = "NC", number = "NC 123349/015229", title = "COMPRA QUIOSQUE", amount = 99.99, date = "20220223", nif = 509441130, iva = 1.66, status = true)
        InvoiceCard(type = "RC", number = "RC 732340010619/123", title = "COMPRA RAMONA", amount = 4.2, date = "20220224", nif = 509441130, iva = 1.66, status = true)
        InvoiceCard(type = "OR", number = "OR 123652201010619/019", title = "COMPRA GLICINIAS PLAZA", amount = 4.2, date = "20230227", nif = 509441130, iva = 1.66, status = false)
        InvoiceCard(type = "FS", number = "FS 2142/210", title = "COMPRA TEXTO EXTREMAMENTE LARGO OVERFLOW", amount = 111.69, date = "20230720", nif = 509441130, iva = 1.66, status = true)
    }
}
