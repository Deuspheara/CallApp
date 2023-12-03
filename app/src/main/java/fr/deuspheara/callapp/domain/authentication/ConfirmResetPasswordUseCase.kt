package fr.deuspheara.callapp.domain.authentication

import fr.deuspheara.callapp.data.repository.authentication.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.domain.authentication.ConfirmResetPasswordUseCase
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Confirm reset password with code
 *
 */
class ConfirmResetPasswordUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {

    private companion object {
        private const val TAG = "ConfirmResetPasswordUseCase"
    }

    suspend operator fun invoke(oobCode: String, password: String): Flow<Boolean> {
        return authenticationRepository.confirmResetPassword(
            oobCode,
            password
        )
    }
}