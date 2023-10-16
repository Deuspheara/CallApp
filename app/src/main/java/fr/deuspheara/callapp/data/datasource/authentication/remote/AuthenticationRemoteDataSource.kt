package fr.deuspheara.callapp.data.datasource.authentication.remote

import kotlinx.coroutines.flow.Flow
import java.time.Instant

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.datasource.authentication.remote.AuthenticationRemoteDataSource
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Authentication remote datasource
 *
 */
interface AuthenticationRemoteDataSource {

    /**
     * Registers a new user using their email and password.
     *
     * @param email User's email address.
     * @param password User's chosen password.
     *
     * @return Flow emitting the user's unique UID upon successful registration.
     */
    fun signUpWithPassword(email: String, password: String): Flow<String>

    /**
     * Authenticates a user using their email and password.
     *
     * @param email User's email address.
     * @param password User's password.
     *
     * @return Flow emitting the user's unique UID upon successful authentication.
     */
    fun signInWithPassword(email: String, password: String): Flow<String>

    /**
     * Checks if there's an authenticated user session active.
     *
     * @return Flow emitting `true` if a user is currently authenticated, `false` otherwise.
     */
    fun isUserAuthenticated(): Flow<Boolean>
}
