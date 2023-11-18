package fr.deuspheara.callapp.domain.user

import fr.deuspheara.callapp.data.datasource.user.model.UserPublicModel
import fr.deuspheara.callapp.data.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.domain.user.GetPublicUserDetailsUseCase
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Get public user details
 *
 */
class GetPublicUserDetailsUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    private companion object {
        private const val TAG = "GetPublicUserDetailsUseCase"
    }

    suspend operator fun invoke(identifier: String): Flow<UserPublicModel?> {
        return userRepository.getPublicUserDetails(identifier)
    }

}