package fr.deuspheara.callapp.data.repository.user

import android.provider.ContactsContract.CommonDataKinds.Phone
import fr.deuspheara.callapp.core.model.text.Email
import fr.deuspheara.callapp.core.model.text.PhoneNumber
import fr.deuspheara.callapp.core.model.user.UserFullModel
import fr.deuspheara.callapp.core.model.user.UserLightModel
import fr.deuspheara.callapp.data.datasource.user.model.UserPublicModel
import fr.deuspheara.callapp.data.datasource.user.model.UserRemoteModel
import kotlinx.coroutines.flow.Flow

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.repository.user.UserRepository
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * User repository
 *
 */
interface UserRepository {

    /**
     * Registers a new user.
     *
     * @param uid User's unique identifier.
     * @param pseudonym User's chosen pseudonym or username.
     * @param firstName User's first name.
     * @param lastName User's last name.
     * @param email User's email address.
     * @param profilePictureUrl URL pointing to the user's profile picture.
     * @param bio User's bio or status message.
     *
     * @return Flow emitting the user's unique UID upon successful registration.
     */
    suspend fun registerUser(
        uid: String,
        identifier: String,
        pseudonym: String,
        firstName: String,
        lastName: String,
        email: Email,
        profilePictureUrl: String?,
        bio: String?,
        phoneNumber: PhoneNumber?
    ): Flow<String?>

    /**
     * Fetches details for a specific user.
     *
     * @param uid User's unique identifier.
     *
     * @return Flow emitting the detailed information of the specified user.
     */
    suspend fun getUserDetails(uid: String): Flow<UserFullModel?>

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
    suspend fun updateUserDetails(
        uid: String,
        displayName: String?,
        firstName: String?,
        lastName: String?,
        email: String?,
        profilePictureUrl: String?,
        bio: String?
    ): Flow<UserLightModel?>

    /**
     * Adds a contact to the user's contact list.
     *
     * @param uid User's unique identifier.
     * @param contactUid UID of the contact to be added.
     *
     * @return Flow signaling the completion of the addition.
     */
    suspend fun addContactToUser(uid: String, contactUid: String): Flow<UserLightModel?>

    /**
     * Removes a contact from the user's contact list.
     *
     * @param uid User's unique identifier.
     * @param contactUid UID of the contact to be removed.
     *
     * @return Flow signaling the completion of the removal.
     */
    suspend fun removeContactFromUser(uid: String, contactUid: String): Flow<UserLightModel?>

    /**
     * Retrieves the list of contacts for a user.
     *
     * @param uid User's unique identifier.
     *
     * @return Flow emitting the list of contact UIDs.
     */
    suspend fun getUserContacts(uid: String): Flow<List<String>>

    /**
     * Retrieves the public information of a user.
     * This information is available to anyone.
     *
     * @return Flow emitting the public information of the user.
     */
    suspend fun getPublicUserDetails(): Flow<List<UserPublicModel>>

    /**
     * Retrieves the public information of a user.
     * This information is available to anyone.
     *
     * @param identifier User's unique identifier.
     * @return Flow emitting the public information of the user.
     */
    suspend fun getPublicUserDetails(identifier: String): Flow<UserPublicModel>

}
