package fr.deuspheara.callapp.domain.user

import android.util.Log
import fr.deuspheara.callapp.core.model.text.Email
import fr.deuspheara.callapp.core.model.text.PhoneNumber
import fr.deuspheara.callapp.core.model.user.UserLightModel
import fr.deuspheara.callapp.data.repository.authentication.AuthenticationRepository
import fr.deuspheara.callapp.data.repository.user.UserRepository
import fr.deuspheara.callapp.domain.authentication.GetCurrentLocalUserUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.domain.user.UpdateUserDetailsUseCase
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Update user details
 *
 */
class UpdateUserDetailsUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val authenticationRepository: AuthenticationRepository,
) {
    private companion object {
        private const val TAG = "UpdateUserDetailsUseCase"
    }

    suspend operator fun invoke(
        displayName: String?,
        firstName: String?,
        lastName: String?,
        email: Email?,
        profilePictureUrl: String?,
        bio: String?,
        phoneNumber: PhoneNumber?
    ) : Flow<UserLightModel?> {
        return  authenticationRepository.getCurrentUser()
            .flatMapLatest { user ->
                Log.d(
                    TAG,
                    "invoke: $displayName $firstName $lastName $email $profilePictureUrl $bio $phoneNumber"
                )
                user?.let {
                    userRepository.updateUserDetails(
                        uid = it.uid,
                        displayName = displayName,
                        firstName = firstName,
                        lastName = lastName,
                        email = email,
                        profilePictureUrl = profilePictureUrl,
                        bio = bio,
                        phoneNumber = phoneNumber
                    )
                } ?: flowOf(null)
            }.catch { e ->
                Log.e(TAG, "Error while getting current user", e)
                throw e
            }
    }
}
