package fr.deuspheara.callapp.domain.authentication

import android.util.Log
import fr.deuspheara.callapp.data.repository.authentication.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.domain.authentication.SendPasswordResetEmailUseCase
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Send password reset email
 *
 */
class SendPasswordResetEmailUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    private companion object {
        private const val TAG = "SendPasswordResetEmailUseCase"
    }

    suspend operator fun invoke(email: String): Flow<Boolean?> {
        return authenticationRepository.sendPasswordResetEmail(email)
            .catch {
                Log.e(TAG, "invoke: ", it)
                emit(false)
            }
    }
}