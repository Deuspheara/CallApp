package fr.deuspheara.callapp.data.repository.authentication

import kotlinx.coroutines.flow.Flow

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
    suspend fun signUpWithPassword(email: String, password: String): Flow<String>

    /**
     * Authenticates a user using their email and password.
     *
     * @param email User's email address.
     * @param password User's password.
     *
     * @return Flow emitting the user's unique UID upon successful authentication.
     */
    suspend fun signInWithPassword(email: String, password: String): Flow<String>

    /**
     * Checks if there's an authenticated user session active.
     *
     * @return Flow emitting `true` if a user is currently authenticated, `false` otherwise.
     */
    suspend fun isUserAuthenticated(): Flow<Boolean>
}
