package fr.deuspheara.callapp.domain.user

import android.util.Log
import fr.deuspheara.callapp.data.datasource.user.model.UserPublicModel
import fr.deuspheara.callapp.data.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.domain.user.getPublicUsersUseCase
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 *
 *
 */
class GetPublicUsersUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {

    private companion object {
        private const val TAG: String = "GetPublicUsersUseCase"
    }

    suspend operator fun invoke(): Flow<List<UserPublicModel>> {
        return userRepository.getPublicUserDetails().catch {
            Log.e(TAG, "invoke: ", it)
            emit(emptyList())
        }
    }
}