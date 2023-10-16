package fr.deuspheara.callapp.data.datasource.user.remote

import fr.deuspheara.callapp.data.datasource.user.model.UserRemote
import kotlinx.coroutines.flow.Flow

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.datasource.user.remote.UserRemoteDataSource
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * User remote datasource
 *
 */
interface UserRemoteDataSource {

    /**
     * Registers a new user.
     *
     * @param uid User's unique identifier.
     * @param pseudonym User's chosen pseudonym or username.
     * @param realName User's real name.
     * @param email User's email address.
     * @param profilePictureUrl URL pointing to the user's profile picture.
     * @param bio User's bio or status message.
     *
     * @return Flow emitting the user's unique UID upon successful registration.
     */
    fun registerUser(
        uid: String,
        pseudonym: String,
        realName: String,
        email: String,
        profilePictureUrl: String?,
        bio: String?
    ): Flow<String>

    /**
     * Fetches details for a specific user.
     *
     * @param uid User's unique identifier.
     *
     * @return Flow emitting the detailed information of the specified user.
     */
    fun getUserDetails(uid: String): Flow<UserRemote>

    /**
     * Updates the information of a user.
     *
     * @param uid User's unique identifier.
     * @param pseudonym Updated pseudonym or username.
     * @param realName Updated real name.
     * @param email Updated email address.
     * @param profilePictureUrl Updated URL for the user's profile picture.
     * @param bio Updated bio or status message.
     *
     * @return Flow signaling the completion of the update.
     */
    fun updateUserDetails(
        uid: String,
        pseudonym: String?,
        realName: String?,
        email: String?,
        profilePictureUrl: String?,
        bio: String?
    ): Flow<UserRemote>

    /**
     * Adds a contact to the user's contact list.
     *
     * @param uid User's unique identifier.
     * @param contactUid UID of the contact to be added.
     *
     * @return Flow signaling the completion of the addition.
     */
    fun addContactToUser(uid: String, contactUid: String): Flow<UserRemote>

    /**
     * Removes a contact from the user's contact list.
     *
     * @param uid User's unique identifier.
     * @param contactUid UID of the contact to be removed.
     *
     * @return Flow signaling the completion of the removal.
     */
    fun removeContactFromUser(uid: String, contactUid: String): Flow<UserRemote>

    /**
     * Retrieves the list of contacts for a user.
     *
     * @param uid User's unique identifier.
     *
     * @return Flow emitting the list of contact UIDs.
     */
    fun getUserContacts(uid: String): Flow<List<String>>

}
