package fr.deuspheara.callapp.domain.user

import fr.deuspheara.callapp.core.model.user.UserFullModel
import fr.deuspheara.callapp.data.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.domain.user.GetUserDetailsUseCase
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Get user details
 *
 */
class GetUserDetailsUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    private companion object {
        private const val TAG = "GetUserDetailsUseCase"
    }

    suspend operator fun invoke(uid: String): Flow<UserFullModel?> {
        return userRepository.getUserDetails(uid)
    }

}
