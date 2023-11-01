package pt.cm.faturasua.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pt.cm.faturasua.data.Invoice

class UserViewModel: ViewModel() {
    val name: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val receiptsList: MutableLiveData<ArrayList<Invoice>> by lazy {
        MutableLiveData<ArrayList<Invoice>>()
    }

    val darkThemePreference by lazy {
        MutableLiveData<Boolean>(false)
    }
}