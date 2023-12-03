package fr.deuspheara.callapp.domain.authentication

import fr.deuspheara.callapp.data.repository.authentication.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.domain.authentication.GetCurrentUserUidUseCase
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Get current user uid
 *
 */
class GetCurrentUserUidUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    private companion object {
        const val TAG = "GetCurrentUserUidUseCase"
    }

    suspend operator fun invoke(): Flow<String?> =
        authenticationRepository
            .getCurrentUser().map {
                it?.uid
            }
}