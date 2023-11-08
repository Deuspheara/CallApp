package fr.deuspheara.callapp.domain.authentication

import android.util.Log
import fr.deuspheara.callapp.data.repository.authentication.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.domain.authentication.IsUserAuthenticatedUseCase
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Give information on wether user is authenticated
 *
 */
class IsUserAuthenticatedUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
) {
    private companion object {
        private const val TAG: String = "IsUserAuthenticatedUseCase"
    }

    suspend operator fun invoke(): Flow<Boolean> {
        return authenticationRepository.isUserAuthenticated().catch {
            Log.e(TAG, "invoke: ", it)
            emit(false)
        }
    }
}

