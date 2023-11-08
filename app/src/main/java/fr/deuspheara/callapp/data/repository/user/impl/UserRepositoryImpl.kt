package fr.deuspheara.callapp.data.repository.user.impl

import android.provider.ContactsContract.CommonDataKinds.Phone
import fr.deuspheara.callapp.core.coroutine.DispatcherModule
import fr.deuspheara.callapp.core.model.text.Email
import fr.deuspheara.callapp.core.model.text.PhoneNumber
import fr.deuspheara.callapp.core.model.user.UserFullModel
import fr.deuspheara.callapp.core.model.user.UserLightModel
import fr.deuspheara.callapp.data.datasource.user.model.UserRemoteFirestoreModel
import fr.deuspheara.callapp.data.datasource.user.remote.UserRemoteDataSource
import fr.deuspheara.callapp.data.repository.user.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.repository.user.impl.UserRepositoryImpl
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Implementation of [UserRepository]
 *
 */
class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    @DispatcherModule.DefaultDispatcher private val defaultContext: CoroutineDispatcher
) : UserRepository {
    override suspend fun registerUser(
        uid: String,
        pseudonym: String,
        firstName: String,
        lastName: String,
        email: Email,
        profilePictureUrl: String?,
        bio: String?,
        phoneNumber: PhoneNumber?
    ): Flow<String?> = withContext(defaultContext) {
        remoteDataSource.registerUser(
            uid = uid,
            displayName = pseudonym,
            firstName = firstName,
            lastName = lastName,
            email = email.value,
            photoUrl = profilePictureUrl ?: "",
            bio = bio ?: "",
            phoneNumber = phoneNumber?.value ?: "",
        )
    }

    override suspend fun getUserDetails(uid: String): Flow<UserFullModel> =
        withContext(defaultContext) {
            remoteDataSource.getUserDetails(uid).map { it.toUserFullModel() }
        }

    override suspend fun updateUserDetails(
        uid: String,
        displayName: String?,
        firstName: String?,
        lastName: String?,
        email: String?,
        profilePictureUrl: String?,
        bio: String?
    ): Flow<UserLightModel?> = withContext(defaultContext) {
        remoteDataSource.updateUserDetails(
            uid = uid,
            displayName = displayName,
            firstName = firstName,
            lastName = lastName,
            email = email,
            profilePictureUrl = profilePictureUrl,
            bio = bio
        ).map { it?.toUserLightModel() }
    }

    override suspend fun addContactToUser(uid: String, contactUid: String): Flow<UserLightModel> =
        withContext(defaultContext) {
            remoteDataSource.addContactToUser(uid, contactUid).map { it.toUserLightModel() }
        }

    override suspend fun removeContactFromUser(
        uid: String,
        contactUid: String
    ): Flow<UserLightModel> = withContext(defaultContext) {
        remoteDataSource.removeContactFromUser(uid, contactUid).map { it.toUserLightModel() }
    }

    override suspend fun getUserContacts(uid: String): Flow<List<String>> =
        withContext(defaultContext) {
            remoteDataSource.getUserContacts(uid)
        }

    fun UserRemoteFirestoreModel.toUserLightModel(): UserLightModel {
        return UserLightModel(
            uuid = uid,
            displayName = this.displayName,
            photoUrl = photoUrl,

            )
    }

     fun UserRemoteFirestoreModel.toUserFullModel(): UserFullModel {
        return UserFullModel(
            uid = uid,
            displayName = displayName,
            firstName = firstName,
            lastName = lastName,
            email = email,
            photoUrl = photoUrl,
            bio = bio,
            contactList = contacts,
        )
    }
}