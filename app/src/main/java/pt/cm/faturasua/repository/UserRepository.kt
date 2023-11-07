package pt.cm.faturasua.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.cm.faturasua.data.Profile
import pt.cm.faturasua.database.UserDatabase

class UserRepository(private val database: UserDatabase) {

    val profile: LiveData<Profile> = database.userDao.getProfile().map { it }

    suspend fun registerProfile(profile: Profile) {
        withContext(Dispatchers.IO) {
            database.userDao.updateProfile(profile)
        }
    }
}