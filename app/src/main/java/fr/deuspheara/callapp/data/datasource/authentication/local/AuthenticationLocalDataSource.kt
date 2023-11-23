package fr.deuspheara.callapp.data.datasource.authentication.local

import fr.deuspheara.callapp.core.model.text.Identifier
import fr.deuspheara.callapp.data.database.model.LocalUserEntity
import kotlinx.coroutines.flow.Flow

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.datasource.authentication.local.AuthenticationLocalDataSource
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 *
 *
 */
interface AuthenticationLocalDataSource {

    /**
     * Inserts a user entity into the local database.
     *
     * @param entity The user entity to be inserted.
     */
    suspend fun insertUser(entity: LocalUserEntity): Flow<Long>

    /**
     * Retrieves a user entity from the local database based on the provided email address.
     *
     * @param email The email address of the user.
     * @return A Flow emitting the user entity corresponding to the provided email, or null if not found.
     */
    fun getUserByEmail(email: String): Flow<LocalUserEntity?>

    /**
     * Retrieves a user entity from the local database based on the provided unique identifier (firestore_uuid).
     *
     * @param uid The unique identifier of the user.
     * @return A Flow emitting the user entity corresponding to the provided unique identifier, or null if not found.
     */
    fun getUserByUid(uid: String): Flow<LocalUserEntity?>

    /**
     * Deletes a user entity from the local database based on the provided unique identifier (firestore_uuid).
     *
     * @param uid The unique identifier of the user to be deleted.
     * @return A Flow emitting the number of users deleted (should be 1 if successful).
     */
    suspend fun deleteUserWithUid(uid: String): Flow<Int>

    /**
     * Updates a user entity in the local database based on the provided unique identifier (firestore_uuid).
     *
     * @param uid The unique identifier of the user to be updated.
     * @param displayName The updated display name of the user.
     * @param photoUrl The updated photo URL of the user.
     * @param email The updated email address of the user.
     * @param phoneNumber The updated phone number of the user.
     * @param isEmailVerified The updated email verification status of the user.
     */
    suspend fun updateUserWithUid(
        uid: String,
        identifier: Identifier?,
        displayName: String?,
        firstname: String?,
        lastname: String?,
        photoUrl: String?,
        email: String?,
        phoneNumber: String?,
        isEmailVerified: Boolean?,
        bio: String?,
        contactList: List<String>?,
        providerId: String?,
    ): Flow<Int>

    /**
     * Inserts a user entity into the local database or updates an existing one based on the unique identifier (firestore_uuid).
     * If a user with the provided unique identifier does not exist, a new user is inserted. If the user already exists,
     * the existing user is updated with the information from the provided [LocalUserEntity].
     *
     * @param localUserEntity The user entity to be inserted or updated.
     * @return A Flow emitting a result code, identifier, or any specific value related to the insertion or update.
     */
    suspend fun insertOrUpdateUser(localUserEntity: LocalUserEntity): Flow<Long>

    /**
     * Delete all users from the local database.
     * @return A Flow emitting the number of users deleted.
     */
    suspend fun deleteUser(): Flow<Int>
}
