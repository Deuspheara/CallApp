package fr.deuspheara.callapp.domain.user

import android.util.Log
import fr.deuspheara.callapp.core.model.user.UserFullModel
import fr.deuspheara.callapp.data.repository.authentication.AuthenticationRepository
import fr.deuspheara.callapp.data.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.domain.user.GetCurrentUserInformationUseCase
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Get current user information usecase
 *
 */
class GetCurrentUserInformationUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val authenticationRepository: AuthenticationRepository
) {
    private companion object {
        private const val TAG = "GetCurrentUserInformationUseCase"
    }

    suspend operator fun invoke(): Flow<UserFullModel?> {
        return authenticationRepository.getCurrentUser()
            .flatMapLatest { user ->
                user?.let {
                    userRepository.getUserDetails(it.uid)
                } ?: flowOf(null)
            }
            .catch { e ->
                Log.e(TAG, "Error fetching user details", e)
                emit(null)
            }
    }
}

