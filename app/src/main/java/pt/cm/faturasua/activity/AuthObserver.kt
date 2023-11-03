package pt.cm.faturasua.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.cm.faturasua.utils.FirebaseUtil

class AuthObserver(
    private val activity: Context,
    private val firebaseUtil: FirebaseUtil
): DefaultLifecycleObserver {
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private var _isUserSignedIn = MutableStateFlow(false)
    val isUserSignedIn = _isUserSignedIn.asStateFlow()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        owner.lifecycleScope.launch {
            _isUserSignedIn.value = firebaseUtil.isUserSignedIn(this).value
        }

    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        val currentUser = firebaseUtil.firebaseAuth.currentUser
        if (currentUser != null) {

        }

    }


    suspend fun onSignOut() {
        _isUserSignedIn.value = false

    }
}