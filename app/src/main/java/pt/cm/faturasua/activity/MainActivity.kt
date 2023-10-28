package pt.cm.faturasua.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.FaturasUATheme
import com.google.android.gms.maps.model.LatLng
import org.koin.core.parameter.parametersOf
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.startKoin
import pt.cm.faturasua.screens.MainScreen
import pt.cm.faturasua.screens.MapsScreen
import pt.cm.faturasua.utils.AppModules
import pt.cm.faturasua.utils.ReceiptNotificationService
import pt.cm.faturasua.viewmodel.UserViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidContext(this@MainActivity)
            modules(AppModules)
        }

        val authObserver:AuthObserver by inject { parametersOf(this@MainActivity) }
        this.lifecycle.addObserver(authObserver)

        val notificationChannel= NotificationChannel(
            ReceiptNotificationService.NOTIFICATION_MSG_ID,
            "Receipts",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)


        fun toScanQRCodeActivity(){
            val intent = Intent(this, ScanActivity::class.java)
            startActivity(intent)
        }


        setContent {
            FaturasUATheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val isUserSignedIn = authObserver.isUserSignedIn.collectAsState().value
                    MainScreen(::toScanQRCodeActivity)
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
}