package pt.cm.faturasua.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pt.cm.faturasua.data.Receipt

class UserViewModel: ViewModel() {
    val name: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val receiptsList: MutableLiveData<ArrayList<Receipt>> by lazy {
        MutableLiveData<ArrayList<Receipt>>()
    }

    val darkThemePreference by lazy {
        MutableLiveData<Boolean>(false)
    }
}