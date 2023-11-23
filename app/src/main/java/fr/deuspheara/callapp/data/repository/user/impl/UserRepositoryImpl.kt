package fr.deuspheara.callapp.data.repository.user.impl

import android.util.Log
import fr.deuspheara.callapp.core.coroutine.DispatcherModule
import fr.deuspheara.callapp.core.model.text.Email
import fr.deuspheara.callapp.core.model.text.PhoneNumber
import fr.deuspheara.callapp.core.model.user.UserFullModel
import fr.deuspheara.callapp.core.model.user.UserLightModel
import fr.deuspheara.callapp.data.database.model.LocalUserEntity
import fr.deuspheara.callapp.data.datasource.authentication.local.AuthenticationLocalDataSource
import fr.deuspheara.callapp.data.datasource.user.model.UserPublicModel
import fr.deuspheara.callapp.data.datasource.user.model.UserRemoteFirestoreModel
import fr.deuspheara.callapp.data.datasource.user.remote.UserRemoteDataSource
import fr.deuspheara.callapp.data.repository.user.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
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
    private val localDataSource: AuthenticationLocalDataSource,
    @DispatcherModule.DefaultDispatcher private val defaultContext: CoroutineDispatcher
) : UserRepository {
    private companion object {
        private const val TAG = "UserRepositoryImpl"
    }

    override suspend fun registerUser(
        uid: String,
        identifier: String,
        pseudonym: String,
        firstName: String,
        lastName: String,
        email: Email,
        profilePictureUrl: String?,
        bio: String?,
        phoneNumber: PhoneNumber?
    ): Flow<String?> = flow {
        try {
            val remoteRegistrationResult = remoteDataSource.registerUser(
                uid = uid,
                identifier = identifier,
                displayName = pseudonym,
                firstName = firstName,
                lastName = lastName,
                email = email.value,
                photoUrl = profilePictureUrl ?: "",
                bio = bio ?: "",
                phoneNumber = phoneNumber?.value ?: "",
            ).first()

            Log.d(TAG, "Remote user registration successful. UID: $remoteRegistrationResult")

            val localInsertResult = localDataSource.insertUser(
                LocalUserEntity(
                    fireStoreUUID = remoteRegistrationResult,
                    displayName = pseudonym,
                    identifier = identifier,
                    firstName = firstName,
                    lastName = lastName,
                    email = email.value,
                    photoUrl = profilePictureUrl ?: "",
                    bio = bio ?: "",
                    phoneNumber = phoneNumber?.value ?: "",
                    isEmailVerified = false,
                    contactList = emptyList(),
                    providerId = "password",
                )
            ).first()

            Log.d(TAG, "Local user insertion successful. UID: $localInsertResult")

            emit(remoteRegistrationResult)
        } catch (e: Exception) {
            // Handle exceptions appropriately
            Log.e(TAG, "Error during user registration", e)
            emit(null) // Emit a null value to indicate failure
        }
    }.flowOn(defaultContext)


    override suspend fun getUserDetails(uid: String): Flow<UserFullModel?> =
        remoteDataSource.getUserDetails(uid).map { it?.toUserFullModel() }

    override suspend fun updateUserDetails(
        uid: String,
        displayName: String?,
        firstName: String?,
        lastName: String?,
        email: Email?,
        profilePictureUrl: String?,
        bio: String?,
        phoneNumber: PhoneNumber?
    ): Flow<UserLightModel?> = remoteDataSource.updateUserDetails(
        uid = uid,
        displayName = displayName,
        firstName = firstName,
        lastName = lastName,
        email = email,
        profilePictureUrl = profilePictureUrl,
        bio = bio,
        phoneNumber = phoneNumber
    ).map { user ->
        Log.d(TAG, "updateUserDetails: $user")
        user?.toUserLightModel()
    }.onEach { user ->
        user?.let {
            localDataSource.updateUserWithUid(
                uid = uid,
                displayName = displayName,
                firstname = firstName,
                lastname = lastName,
                email = email?.value,
                photoUrl = profilePictureUrl,
                bio = bio,
                phoneNumber = phoneNumber?.value,
                isEmailVerified = true,
                contactList = null,
                providerId = null,
                identifier = null
            ).first()
        }
    }.flowOn(defaultContext)

    override suspend fun addContactToUser(uid: String, contactUid: String): Flow<UserLightModel?> =
        withContext(defaultContext) {
            remoteDataSource.addContactToUser(uid, contactUid).map { it?.toUserLightModel() }
        }

    override suspend fun removeContactFromUser(
        uid: String,
        contactUid: String
    ): Flow<UserLightModel?> = withContext(defaultContext) {
        remoteDataSource.removeContactFromUser(uid, contactUid).map { it?.toUserLightModel() }
    }

    override suspend fun getUserContacts(uid: String): Flow<List<String>> =
        withContext(defaultContext) {
            remoteDataSource.getUserContacts(uid)
        }

    override suspend fun getPublicUserDetails(): Flow<List<UserPublicModel>> =
        withContext(defaultContext) {
            remoteDataSource.getPublicUserDetails()
        }

    override suspend fun getPublicUserDetails(identifier: String): Flow<UserPublicModel> {
        return withContext(defaultContext) {
            remoteDataSource.getPublicUserDetails(identifier)
        }
    }

    override suspend fun insertLocalUser(
        uid: String,
        identifier: String,
        displayName: String,
        firstName: String,
        lastName: String,
        email: Email,
        profilePictureUrl: String?,
        bio: String?,
        phoneNumber: PhoneNumber?
    ): Flow<Long?> {
        return localDataSource.insertUser(
            LocalUserEntity(
                fireStoreUUID = uid,
                displayName = displayName,
                identifier = identifier,
                firstName = firstName,
                lastName = lastName,
                email = email.value,
                photoUrl = profilePictureUrl ?: "",
                bio = bio ?: "",
                phoneNumber = phoneNumber?.value ?: "",
                isEmailVerified = false,
                contactList = emptyList(),
                providerId = "password",
            )
        )
    }

    private fun UserRemoteFirestoreModel.toUserLightModel(): UserLightModel {
        return UserLightModel(
            uuid = uid,
            displayName = this.displayName,
            photoUrl = photoUrl
        )
    }

    private fun UserRemoteFirestoreModel.toUserFullModel(): UserFullModel {
        return UserFullModel(
            uid = uid,
            identifier = identifier,
            displayName = displayName,
            firstName = firstName,
            lastName = lastName,
            email = email,
            photoUrl = photoUrl,
            bio = bio,
            contactList = contacts,
        )
    }

    private fun UserRemoteFirestoreModel.toLocalUserEntity(): LocalUserEntity {
        return LocalUserEntity(
            fireStoreUUID = uid,
            displayName = displayName,
            identifier = identifier,
            firstName = firstName,
            lastName = lastName,
            email = email,
            photoUrl = photoUrl,
            bio = bio,
            phoneNumber = phoneNumber,
            isEmailVerified = false,
            contactList = contacts,
            providerId = "password",
        )
    }
}