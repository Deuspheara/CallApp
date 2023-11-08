package fr.deuspheara.callapp.domain.authentication

import android.util.Log
import fr.deuspheara.callapp.core.model.text.Email
import fr.deuspheara.callapp.core.model.text.Password
import fr.deuspheara.callapp.data.repository.authentication.AuthenticationRepository
import fr.deuspheara.callapp.data.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.domain.authentication.SignUpWithEmailPasswordUseCase
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Sign up user with email and password
 *
 */
class SignUpWithEmailPasswordUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val userRepository: UserRepository,
) {

    private companion object {
        private const val TAG: String = "SignUpWithEmailPasswordUseCase"
    }

    suspend operator fun invoke(
        email: Email,
        password: Password,
        pseudonym: String,
        firstName: String,
        lastName: String,
        profilePictureUrl: String?,
        bio: String?
    ): Flow<String?> {

        return authenticationRepository.signUpWithPassword(email.value, password.value)
            .flatMapLatest {
                userRepository.registerUser(
                    uid = it,
                    pseudonym = pseudonym,
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    profilePictureUrl = profilePictureUrl,
                    bio = bio,
                    phoneNumber = null
                )
            }.catch {
                Log.e(TAG, "invoke: ", it)
                emit(null)
            }

    }

}