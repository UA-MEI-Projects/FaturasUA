package pt.cm.faturasua.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pt.cm.faturasua.data.Invoice
import pt.cm.faturasua.data.Profile

class UserViewModel: ViewModel() {
    val name: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val receiptsList: MutableLiveData<ArrayList<Invoice>> by lazy {
        MutableLiveData<ArrayList<Invoice>>(
            ArrayList()
        )
    }

    val darkThemePreference by lazy {
        MutableLiveData<Boolean>(false)
    }

    val notifsOn by lazy {
        MutableLiveData<Boolean>(true)
    }

    val profile:MutableLiveData<Profile> by lazy {
        MutableLiveData<Profile>(
            Profile()
        )
    }
}