package pt.cm.faturasua.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose.FaturasUATheme
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.startKoin
import pt.cm.faturasua.screens.MainScreen
import pt.cm.faturasua.utils.AppModules
import pt.cm.faturasua.utils.PreferencesManager
import pt.cm.faturasua.utils.ReceiptNotificationService
import pt.cm.faturasua.viewmodel.UserViewModel
import org.koin.android.ext.android.get
import pt.cm.faturasua.utils.FirebaseUtil


class MainActivity : ComponentActivity() {
    private val userViewModel:UserViewModel by viewModel()
    private val firebaseUtil:FirebaseUtil = get()

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

        userViewModel.darkThemePreference.setValue(PreferencesManager(context = this)
                          .getData(PreferencesManager.PREFERENCE_CODE_DARK_MODE, false))

        userViewModel.darkThemePreference.observe(this){
            recreate()
        }

        val receiptNotificationService = ReceiptNotificationService(this)

        firebaseUtil.receiptsListener(receiptNotificationService)

        setContent {
            FaturasUATheme(
                darkTheme = userViewModel.darkThemePreference.value!!
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val scope = rememberCoroutineScope()
                    val isUserSignedIn = authObserver.isUserSignedIn.collectAsState().value
                    if(isUserSignedIn){
                        try {
                            userViewModel.name.setValue(firebaseUtil.user.displayName)
                        }catch (e :Exception){
                            e.printStackTrace()
                        }


                        MainScreen(
                            onSignOut = {
                                scope.launch { authObserver.onSignOut() }
                            }
                        )
                    }
                    else{
                        authObserver.onSignUp()
                    }
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