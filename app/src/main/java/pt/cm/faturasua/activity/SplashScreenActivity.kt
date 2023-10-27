package pt.cm.faturasua.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose.FaturasUATheme
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import pt.cm.faturasua.utils.AuthUtil

class SplashScreenActivity : ComponentActivity() {

    private fun signInCallback() {
        val authUtil = AuthUtil()

        val signInLauncher = registerForActivityResult(
            FirebaseAuthUIActivityResultContract(),
        ) { res ->
            authUtil.onSignInResult(res)
        }

        signInLauncher.launch(authUtil.themeAndLogo())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        signInCallback()


        setContent {
            FaturasUATheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    FaturasUATheme {
        Greeting2("Android")
    }
}