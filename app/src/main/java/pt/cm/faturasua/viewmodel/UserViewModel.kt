package pt.cm.faturasua.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import pt.cm.faturasua.data.Invoice
import pt.cm.faturasua.data.Profile

class UserViewModel: ViewModel() {
    private val _receiptsList = MutableStateFlow(ArrayList<Invoice>())
    val receiptsList: StateFlow<ArrayList<Invoice>> = _receiptsList.asStateFlow()

    val nif:MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    private val _darkThemePreference = MutableStateFlow<Boolean>(false)
    val darkThemePreference: StateFlow<Boolean> = _darkThemePreference.asStateFlow()

    private val _notifsOn = MutableStateFlow<Boolean>(true)
    val notifsOn: StateFlow<Boolean> = _notifsOn.asStateFlow()

    private val _adminMode = MutableStateFlow<Boolean>(false)
    val adminMode: StateFlow<Boolean> = _adminMode.asStateFlow()

    private val _profile = MutableStateFlow<Profile>(Profile())
    val profile: StateFlow<Profile> = _profile.asStateFlow()


    fun updateTheme(state:Boolean){
        _darkThemePreference.update { state }
    }

    fun updateNotifs(state: Boolean){
        _notifsOn.update { state }
    }

    fun changeToAdminMode(state : Boolean){
        _adminMode.update { state }
    }

    fun updateProfile(profile: Profile){
        _profile.update { profile }
    }

    fun addToReceiptsList(invoice: Invoice){
        _receiptsList.update {
            it.add(invoice)
            it
        }
    }

    fun updateReceiptList(list : ArrayList<Invoice>){
        _receiptsList.update { list }
    }
}