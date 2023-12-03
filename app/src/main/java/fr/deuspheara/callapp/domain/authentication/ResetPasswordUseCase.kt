package fr.deuspheara.callapp.domain.authentication

import android.util.Log
import fr.deuspheara.callapp.data.repository.authentication.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.time.Instant
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.domain.authentication.ResetPasswordUseCase
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Reset password use case
 *
 */
class ResetPasswordUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {

    private companion object {
        private const val TAG = "ResetPasswordUseCase"
    }

    suspend operator fun invoke(password: String): Flow<Instant?> {
        return authenticationRepository.resetPassword(password = password)
            .catch { e ->
                Log.e(TAG, "invoke", e)
                throw e
            }
    }
}