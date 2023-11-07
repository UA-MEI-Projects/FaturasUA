package pt.cm.faturasua.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Observer
import androidx.lifecycle.distinctUntilChanged
import com.example.compose.FaturasUATheme
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pt.cm.faturasua.screens.AdminScreen
import pt.cm.faturasua.screens.AuthScreen
import pt.cm.faturasua.screens.MainScreen
import pt.cm.faturasua.utils.PreferencesManager
import pt.cm.faturasua.utils.ReceiptNotificationService
import pt.cm.faturasua.viewmodel.UserViewModel
import pt.cm.faturasua.utils.FirebaseUtil


class MainActivity : ComponentActivity() {
    private val userViewModel:UserViewModel by viewModels<UserViewModel>()
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val receiptNotificationService = ReceiptNotificationService(this)
        var firebaseUtil = FirebaseUtil(this, firebaseAuth, firebaseDatabase, userViewModel, receiptNotificationService)


        val notificationChannel= NotificationChannel(
            ReceiptNotificationService.NOTIFICATION_MSG_ID,
            "Receipts",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)

        userViewModel.updateTheme(PreferencesManager(context = this)
                          .getData(PreferencesManager.PREFERENCE_CODE_DARK_MODE, false))


        setContent {
            val darkTheme = userViewModel.darkThemePreference.collectAsState().value
            val adminMode = userViewModel.adminMode.collectAsState().value
            FaturasUATheme(
                darkTheme = darkTheme
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val scope = rememberCoroutineScope()
                    val isUserSignedIn = firebaseUtil.isUserSignedIn(scope = scope).collectAsState().value
                    if(adminMode){
                        LaunchedEffect(key1 = true){
                            firebaseUtil.getAllReceiptsFromDB()
                        }

                        AdminScreen(
                            firebaseUtil = firebaseUtil,
                            onSignOutCallback = { userViewModel.changeToAdminMode(!adminMode)}
                        )
                    }
                    else{
                        if(isUserSignedIn){
                            LaunchedEffect(key1 = true){

                                firebaseUtil.getProfile()
                                firebaseUtil.getNif()
                                firebaseUtil.getReceiptsFromDB()
                            }

                            MainScreen(
                                firebaseUtil = firebaseUtil,
                                onSignOut = {
                                    scope.launch { firebaseUtil.signOut() }
                                },
                                userViewModel = userViewModel
                            )
                        }
                        else{
                            AuthScreen(
                                firebaseUtil = firebaseUtil,
                                onChangeAdminMode = {
                                    userViewModel.changeToAdminMode(true)
                                }
                            )
                        }
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