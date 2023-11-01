package pt.cm.faturasua.utils

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import pt.cm.faturasua.R
import pt.cm.faturasua.data.Invoice
import pt.cm.faturasua.viewmodel.UserViewModel
import javax.inject.Inject

class FirebaseUtil  @Inject constructor (
    val authUI: AuthUI,
    val firebaseAuth: FirebaseAuth,
    val firebaseDatabase: FirebaseDatabase,
    val userViewModel: UserViewModel
){
    private lateinit var databaseReference: DatabaseReference
    lateinit var user: FirebaseUser
    val dbReceiptsRef = {
        databaseReference = firebaseDatabase.getReference()
        databaseReference.child("users").child(firebaseAuth.currentUser!!.uid)
    }

    fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == Activity.RESULT_OK){
            Log.d(ContentValues.TAG, "Successfully signed in correctly")
            user = firebaseAuth.currentUser!!
            userViewModel.name.setValue(user.displayName)
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
            .setLogo(R.mipmap.ic_launcher)
            .setTheme(R.style.Theme_FaturasUA)
            .build()

        return signInIntent
    }

    fun onSignOutRequest(activity : Context){
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

    fun addReceiptToDB(receipt: Invoice){
        dbReceiptsRef().child(receipt.id).setValue(receipt)
    }

    fun receiptsListener(notificationCallback: ReceiptNotificationService){
        dbReceiptsRef()
            .addChildEventListener(object : ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                    userViewModel.receiptsList.value!!.add(snapshot.getValue(Invoice::class.java)!!)
                    notificationCallback.sendReceiptAddedNotification()
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    notificationCallback.sendReceiptErrorNotification()
                }

            })
    }

    companion object {
        val SIGN_UP_CODE: String = "101"
    }
}
