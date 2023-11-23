package fr.deuspheara.callapp.domain.authentication

import android.util.Log
import fr.deuspheara.callapp.core.model.text.Email
import fr.deuspheara.callapp.core.model.text.Password
import fr.deuspheara.callapp.core.model.text.PhoneNumber
import fr.deuspheara.callapp.core.model.user.UserFullModel
import fr.deuspheara.callapp.data.repository.authentication.AuthenticationRepository
import fr.deuspheara.callapp.data.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.domain.authentication.SignInWithEmailPasswordUseCase
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Sign in the user with email and password
 *
 */
class SignInWithEmailPasswordUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val userRepository: UserRepository,
) {

    private companion object {
        private const val TAG: String = "SignInWithEmailPasswordUseCase"
    }

    suspend operator fun invoke(email: Email, password: Password): Flow<UserFullModel?> {

        return authenticationRepository.signInWithPassword(email.value, password.value)
            .flatMapLatest { user ->
                userRepository.getUserDetails(user.uid)
            }
            .onEach { user ->
                user?.apply {
                    userRepository.insertLocalUser(
                        uid = uid,
                        email = email,
                        displayName = displayName,
                        firstName = firstName,
                        lastName = lastName,
                        phoneNumber = PhoneNumber(phoneNumber),
                        profilePictureUrl = photoUrl,
                        bio = bio,
                        identifier = identifier
                    ).firstOrNull()
                }

            }.catch {
                Log.e(TAG, "invoke: ", it)
                emit(null)
            }

    }
}