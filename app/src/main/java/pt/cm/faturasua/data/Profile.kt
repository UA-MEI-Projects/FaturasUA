package pt.cm.faturasua.data

import android.net.Uri

data class Profile(
    var name: String = "",
    var email: String = "",
    var photo: Uri = Uri.EMPTY,
    var phoneNumber: String = ""
)
