package pt.cm.faturasua.utils

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import pt.cm.faturasua.R

class FirebaseUtil {
    private lateinit var authUI: AuthUI
    private lateinit var firebaseAuth: FirebaseAuth

    init {
        authUI = AuthUI.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
    }

    fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == Activity.RESULT_OK){
            Log.d(ContentValues.TAG, "Successfully signed in correctly")
            val user = firebaseAuth.currentUser
            user?.let {

            }
        }

    }

    fun themeAndLogo() : Intent {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
        )
        val signInIntent = authUI
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
//            .setLogo(R.drawable.zypho_logo)
            .setTheme(R.style.Theme_FaturasUA)
            .build()

        return signInIntent
    }

    suspend fun onSignOutRequest(activity : Context){
        try{
            authUI.signOut(activity)
        }catch (e :Exception){
            Log.d("SIGN OUT", e.message.toString())
        }
    }

    fun isUserSignedIn(scope : CoroutineScope) = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener {
            trySend(firebaseAuth.currentUser != null)
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }.stateIn(scope, SharingStarted.WhileSubscribed(), firebaseAuth.currentUser != null)

    companion object {
        val SIGN_UP_CODE: String = "101"
    }
}