package pt.cm.faturasua.database

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.cm.faturasua.data.Profile

@Entity
data class DatabaseProfile constructor(
    @PrimaryKey
    val uid: String,
    val name: String,
    val email: String,
    val photo: Uri,
    val phoneNumber: String)


fun DatabaseProfile.mapToEntity() = Profile(
        uid = uid,
        name = name,
        email = email,
        photo = photo,
        phoneNumber = phoneNumber
    )

