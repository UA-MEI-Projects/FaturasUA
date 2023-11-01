package pt.cm.faturasua.utils

import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pt.cm.faturasua.activity.AuthObserver
import pt.cm.faturasua.viewmodel.UserViewModel

val AppModules = module{
    single { FirebaseAuth.getInstance()}
    single { AuthUI.getInstance()}
    single { FirebaseDatabase.getInstance()}
    single { FirebaseUtil(get(), get(), get(), get()) }
    factory { (activityContext : Context) ->
        AuthObserver(activityContext, get())
    }
    viewModel {
        UserViewModel()
    }
}