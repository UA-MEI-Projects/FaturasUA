package pt.cm.faturasua.utils

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import pt.cm.faturasua.R
import pt.cm.faturasua.data.Invoice
import pt.cm.faturasua.data.Profile
import pt.cm.faturasua.viewmodel.UserViewModel

class FirebaseUtil(
    val context:Activity,
    val firebaseAuth: FirebaseAuth,
    val firebaseDatabase: FirebaseDatabase,
    val userViewModel: UserViewModel,
    val receiptNotificationService: ReceiptNotificationService
){
    private lateinit var databaseReference: DatabaseReference
    val dbReceiptsRef = {
        databaseReference = firebaseDatabase.getReference()
        databaseReference.child("users").child(firebaseAuth.currentUser!!.uid)
    }

    fun createAccount(email: String, password: String, displayName: String, phoneNumber: String) {
        // [START create_user_with_email]
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(context) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Auth", "createUserWithEmail:success")
                    val user = firebaseAuth.currentUser!!
                    val profileChangeRequest = UserProfileChangeRequest
                        .Builder()
                        .setDisplayName(displayName)
                        .build()
                    user.updateProfile(profileChangeRequest).addOnCompleteListener {
                        userViewModel.updateProfile(
                            Profile(
                                uid = firebaseAuth.currentUser!!.uid,
                                name = displayName,
                                email = email,
                                phoneNumber = phoneNumber
                            )
                        )
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Auth", "createUserWithEmail:failure", task.exception)

                }
            }
        // [END create_user_with_email]
    }

    fun signIn(email: String, password: String, onSignInResult: (Boolean) -> Unit){
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(context) { task ->
                if (task.isSuccessful) {
                    Log.d("Sign in", "signInWithEmail:success")
                    val user = firebaseAuth.currentUser!!
                    userViewModel.updateProfile(
                        Profile(
                            uid = user.uid,
                            name = user.displayName.toString(),
                            email = user.email.orEmpty(),
                            phoneNumber = user.phoneNumber.orEmpty()
                        ))
                    onSignInResult(true)
                } else {
                    onSignInResult(false)
                    Log.w("Sign in", "signInWithEmail:failure", task.exception)
                }
            }

    }

    fun signOut(){
        try {
            firebaseAuth.signOut()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun updateUserProfile(updatedProfile: Profile){
        val user = firebaseAuth.currentUser!!

        if(userViewModel.profile.value.name != updatedProfile.name ||
            userViewModel.profile.value.photo != updatedProfile.photo){
            val profileChangeRequest = UserProfileChangeRequest
                .Builder()
                .setDisplayName(updatedProfile.name)
                .setPhotoUri(updatedProfile.photo)
                .build()
            user.updateProfile(profileChangeRequest)
        }

        if(userViewModel.profile.value.email != updatedProfile.email){
            user.updateEmail(updatedProfile.email)
        }
    }

    fun updatePassword(password: String){
        val user = firebaseAuth.currentUser!!

        user!!.updatePassword(password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Password Change", "User password updated.")
                }
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

    fun setNIF(nif : String){
        firebaseDatabase.getReference("nif").setValue(nif)
    }

    suspend fun getNif(){
        var nif = ""
        firebaseDatabase.getReference().child("nif").get().addOnCompleteListener {
            if(it.isSuccessful){
                nif = it.result.getValue(String::class.java).toString()
                userViewModel.nif.setValue(nif)
            }
        }
    }

    suspend fun getProfile(){
        val user = firebaseAuth.currentUser!!
        userViewModel.updateProfile(
            Profile(
                uid = user.uid,
                name = user.displayName.toString(),
                email = user.email.orEmpty(),
                phoneNumber = user.phoneNumber.orEmpty()
            ))
    }

    suspend fun getReceiptsFromDB(){
        var receiptsList:ArrayList<Invoice> = ArrayList()
        dbReceiptsRef().get().addOnCompleteListener {
            if(it.isSuccessful){
                val map = it.result.children
                map.forEach{ receipt ->
                        val invoice = receipt.getValue(Invoice::class.java)
                        if (invoice != null) {
                            receiptsList.add(invoice)
                        }
                    }
                }
                userViewModel.updateReceiptList(receiptsList)
            }
    }

    fun getAllReceiptsFromDB(){
        var receiptsList:ArrayList<Invoice> = ArrayList()
        firebaseDatabase.getReference("users").get().addOnCompleteListener {
            if(it.isSuccessful){
                val users = it.result.children
                users.forEach{user ->
                    val invoices = user.children
                    invoices.forEach { receipt ->
                        val invoice = receipt.getValue(Invoice::class.java)
                            if (invoice != null) {
                                receiptsList.add(invoice)
                            }
                    }
                }
                userViewModel.updateReceiptList(receiptsList)
            }
        }
    }

    fun changeReceiptStatus(receiptId:String, userId:String, value: Boolean){
        firebaseDatabase.getReference("users").child(userId).child(receiptId).child("status").setValue(value)
    }

    fun addReceiptToDB(receipt: Invoice){
        userViewModel.addToReceiptsList(receipt)
        dbReceiptsRef().child(receipt.id).setValue(receipt).addOnCompleteListener {
            if(it.isSuccessful && userViewModel.notifsOn.value){
                receiptNotificationService.sendReceiptAddedNotification()
            }
            else if(it.isCanceled){
                receiptNotificationService.sendReceiptErrorNotification()
            }
        }
    }

    companion object {
        val SIGN_UP_CODE: String = "101"
    }
}
