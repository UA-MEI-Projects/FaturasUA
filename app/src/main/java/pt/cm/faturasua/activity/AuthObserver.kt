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
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.compose.getKoinScope
import pt.cm.faturasua.utils.FirebaseUtil
import pt.cm.faturasua.viewmodel.UserViewModel
import javax.inject.Inject

class AuthObserver @Inject constructor(
    private val activity: Context,
    private val firebaseUtil: FirebaseUtil
): DefaultLifecycleObserver {
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private var _isUserSignedIn = MutableStateFlow(false)
    val isUserSignedIn = _isUserSignedIn.asStateFlow()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        val activityResultRegistry = (activity as ComponentActivity).activityResultRegistry

        owner.lifecycleScope.launch {
            _isUserSignedIn.value = firebaseUtil.isUserSignedIn(this).value
        }

        activityResultLauncher = activityResultRegistry.register(
            FirebaseUtil.SIGN_UP_CODE,owner,
            FirebaseAuthUIActivityResultContract(),
        ) { res ->
            owner.lifecycle.coroutineScope.launch {
                firebaseUtil.onSignInResult(res)
                _isUserSignedIn.value = firebaseUtil.isUserSignedIn(this).value
            }
        }


    }

    fun onSignUp(){
        try {
            activityResultLauncher.launch(firebaseUtil.themeAndLogo())
        }catch (e :Exception){
            Log.d("SIGN IN", e.message.toString())
        }
    }

    suspend fun onSignOut() {
        firebaseUtil.onSignOutRequest(activity as ComponentActivity)
        _isUserSignedIn.value = false

    }
}