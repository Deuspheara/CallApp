package fr.deuspheara.callapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import fr.deuspheara.callapp.core.model.text.Identifier
import fr.deuspheara.callapp.data.database.model.LocalUserEntity

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.database.dao.UserDao
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 *
 *
 */
/**
 * Data Access Object (DAO) for managing user entities in the local database.
 */
@Dao
interface UserDao {

    /**
     * Inserts a user entity into the "user" table. If a user with the same unique identifier (firestore_uuid) already exists,
     * it replaces the existing user with the new one.
     *
     * @param entity The user entity to be inserted or updated.
     * @return The row ID of the inserted or updated user entity.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(entity: LocalUserEntity): Long

    /**
     * Retrieves all user entities from the "user" table.
     *
     * @return A list of all user entities in the database.
     */
    @Query("SELECT * FROM user")
    fun getAllUsers(): List<LocalUserEntity>

    /**
     * Retrieves a user entity from the "user" table based on the provided email address.
     *
     * @param email The email address of the user.
     * @return The user entity corresponding to the provided email, or null if not found.
     */
    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): LocalUserEntity?

    /**
     * Retrieves a user entity from the "user" table based on the provided unique identifier (firestore_uuid).
     *
     * @param uid The unique identifier of the user.
     * @return The user entity corresponding to the provided unique identifier, or null if not found.
     */
    @Query("SELECT * FROM user WHERE firestore_uuid = :uid LIMIT 1")
    suspend fun getUserByUid(uid: String): LocalUserEntity?

    /**
     * Deletes a user entity from the "user" table based on the provided unique identifier (firestore_uuid).
     *
     * @param uid The unique identifier of the user to be deleted.
     * @return The number of users deleted (should be 1 if successful).
     */
    @Transaction
    @Query("DELETE FROM user WHERE firestore_uuid = :uid")
    suspend fun deleteUserWithUid(uid: String): Int

    /**
     * Updates a user entity in the "user" table based on the provided unique identifier (firestore_uuid).
     *
     * @param fireStoreUUID The unique identifier of the user to be updated.
     * @param displayName The updated display name of the user.
     * @param firstname The updated first name of the user.
     * @param lastname The updated last name of the user.
     * @param photoUrl The updated photo URL of the user.
     * @param email The updated email address of the user.
     * @param phoneNumber The updated phone number of the user.
     * @param isEmailVerified The updated email verification status of the user.
     * @param bio The updated bio of the user.
     * @param contactList The updated contact list of the user.
     * @param providerId The updated provider id of the user.
     */
    @Query("UPDATE user SET display_name = :displayName,identifier = :identifier, first_name = :firstName, last_name = :lastName, " +
            "photo_url = :photoUrl, email = :email, phone_number = :phoneNumber, " +
            "is_email_verified = :isEmailVerified, bio = :bio, contact_list = :contactList, " +
            "provider_id = :providerId WHERE firestore_uuid = :fireStoreUUID")
    suspend fun updateUserWithUid(
        fireStoreUUID: String,
        identifier: String,
        displayName: String,
        firstName: String,
        lastName: String,
        photoUrl: String,
        email: String,
        phoneNumber: String,
        isEmailVerified: Boolean,
        bio: String,
        contactList: List<String>,
        providerId: String
    ): Int



    /**
     * Inserts a user entity into the "user" table or updates an existing one based on the unique identifier (firestore_uuid).
     * If a user with the provided unique identifier does not exist, a new user is inserted. If the user already exists,
     * the existing user is updated with the information from the provided [LocalUserEntity].
     *
     * @param localUserEntity The user entity to be inserted or updated.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(localUserEntity: LocalUserEntity): Long

    /**
     * Deletes all user entities from the "user" table.
     */
    @Query("DELETE FROM user")
    suspend fun deleteAllUsers() : Int


}