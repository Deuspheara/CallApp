package fr.deuspheara.callapp.data.repository.authentication

import com.google.firebase.auth.ActionCodeResult
import fr.deuspheara.callapp.core.model.user.UserFullModel
import fr.deuspheara.callapp.data.datasource.user.model.UserRemoteModel
import kotlinx.coroutines.flow.Flow
import java.time.Instant

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.repository.authentication.AuthenticationRepository
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Authentication repository
 *
 */
interface AuthenticationRepository {

    /**
     * Registers a new user using their email and password.
     *
     * @param email User's email address.
     * @param password User's chosen password.
     *
     * @return Flow emitting the user's unique UID upon successful registration.
     */
    suspend fun signUpWithPassword(email: String, password: String): Flow<UserRemoteModel>

    /**
     * Authenticates a user using their email and password.
     *
     * @param email User's email address.
     * @param password User's password.
     *
     * @return Flow emitting the user's unique UID upon successful authentication.
     */
    suspend fun signInWithPassword(email: String, password: String): Flow<UserFullModel>

    /**
     * Checks if there's an authenticated user session active.
     *
     * @return Flow emitting `true` if a user is currently authenticated, `false` otherwise.
     */
    suspend fun isUserAuthenticated(): Flow<Boolean>

    /**
     * Sends a password reset email to the user.
     *
     * @param email User's email address.
     *
     * @return Flow emitting `true` if the email was sent successfully, `false` otherwise.
     */
    suspend fun sendPasswordResetEmail(email: String): Flow<Boolean>

    /**
     * Set new perform with code received
     * @param oobCode use to confirm the reset of the password
     * @param password provide the new password
     * @return a [Boolean]
     */
    suspend fun confirmResetPassword(oobCode: String, password: String): Flow<Boolean>

    /**
     * Check action code from generated by sendPasswordResetEmail(String) or FirebaseUser.sendEmailVerification()
     * @param code The code to verify
     * @return a [ActionCodeResult] info on the action code
     */
    suspend fun checkActionCode(code: String): Flow<Boolean>

    /**
     * Enable to reset a password
     * @param password Provide the new password
     *
     * @return an [Instant]
     */
    suspend fun resetPassword(password: String): Flow<Instant?>

    /**
     * Sign out the user
     *
     * @return an [Instant] when the user is sign out
     */
    suspend fun signOut(): Flow<String?>

    /**
     * Get the current user information
     *
     * @return a [UserRemoteModel]
     */

    suspend fun getCurrentUser(): Flow<UserFullModel?>

    /**
     * Get current local user
     * @return a [UserFullModel]
     */
    suspend fun getUserByUid(uid: String): Flow<UserFullModel?>
}
