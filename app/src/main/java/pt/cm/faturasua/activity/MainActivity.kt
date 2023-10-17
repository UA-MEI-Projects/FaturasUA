package pt.cm.faturasua.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import pt.cm.faturasua.screens.MainScreen
import pt.cm.faturasua.ui.theme.FaturasUATheme
import pt.cm.faturasua.utils.AuthUtil

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            FaturasUATheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(::signInCallback)
                }
            }
        }

    }

    private fun signInCallback() {
        val authUtil = AuthUtil()

        val signInLauncher = registerForActivityResult(
            FirebaseAuthUIActivityResultContract(),
        ) { res ->
            authUtil.onSignInResult(res)
        }

        signInLauncher.launch(authUtil.themeAndLogo())
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FaturasUATheme {
        MainScreen({})
    }
}