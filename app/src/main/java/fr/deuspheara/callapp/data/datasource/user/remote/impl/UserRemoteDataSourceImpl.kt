package fr.deuspheara.callapp.data.datasource.user.remote.impl

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.snapshots
import fr.deuspheara.callapp.core.coroutine.DispatcherModule
import fr.deuspheara.callapp.core.model.user.UserLightModel
import fr.deuspheara.callapp.data.datasource.user.model.UserRemoteFirestoreModel
import fr.deuspheara.callapp.data.datasource.user.model.UserRemoteModel
import fr.deuspheara.callapp.data.datasource.user.remote.UserRemoteDataSource
import fr.deuspheara.callapp.data.firebase.FirebaseModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.time.Instant
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.datasource.user.remote.impl.UserRemoteDataSourceImpl
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Implementation of [UserRemoteDataSource]
 *
 */
class UserRemoteDataSourceImpl @Inject constructor(
    @FirebaseModule.UserCollectionReference private val userCollection: CollectionReference,
    @DispatcherModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : UserRemoteDataSource {
    override suspend fun registerUser(
        uid: String,
        email: String,
        displayName: String,
        firstName: String,
        lastName: String,
        phoneNumber : String,
        photoUrl: String,
        bio: String
    ): Flow<String> {
        return flow {
            val newUserRemote = UserRemoteFirestoreModel(
                uid = uid,
                displayName = displayName,
                firstName = firstName,
                lastName = lastName,
                email = email,
                photoUrl = photoUrl,
                bio = bio,
                phoneNumber = phoneNumber,
                contacts = emptyList(),
            )
            userCollection.document(uid).set(newUserRemote).await()
            emit(uid)
        }.flowOn(ioDispatcher)
    }

    override suspend fun getUserDetails(uid: String): Flow<UserRemoteFirestoreModel> {
        return userCollection.document(uid).snapshots()
            .map { snapshot -> snapshot.toObject(UserRemoteFirestoreModel::class.java)!! }
            .flowOn(ioDispatcher)
    }

    override suspend fun updateUserDetails(
        uid: String,
        displayName: String?,
        firstName: String?,
        lastName: String?,
        email: String?,
        profilePictureUrl: String?,
        bio: String?
    ): Flow<UserRemoteFirestoreModel> {
        return flow {
            val updates = mutableMapOf<String, Any?>()
            displayName?.let { updates["displayName"] = it }
            firstName?.let { updates["firstName"] = it }
            lastName?.let { updates["lastName"] = it }
            email?.let { updates["email"] = it }
            profilePictureUrl?.let { updates["photoUrl"] = it }
            bio?.let { updates["bio"] = it }

            userCollection.document(uid).update(updates).await()
            emit(getUserDetails(uid).first())
        }.flowOn(ioDispatcher)
    }

    override suspend fun addContactToUser(uid: String, contactUid: String): Flow<UserRemoteFirestoreModel> {
        return flow {
            val user = getUserDetails(uid).first()
            val updatedContacts = user.contacts.toMutableList().apply { add(contactUid) }
            userCollection.document(uid).update("contacts", updatedContacts).await()
            emit(getUserDetails(uid).first())  // Emit the updated user data after adding the contact
        }.flowOn(ioDispatcher)
    }

    override suspend fun removeContactFromUser(uid: String, contactUid: String): Flow<UserRemoteFirestoreModel> {
        return flow {
            val user = getUserDetails(uid).first()
            val updatedContacts = user.contacts.toMutableList().apply { remove(contactUid) }
            userCollection.document(uid).update("contacts", updatedContacts).await()
            emit(getUserDetails(uid).first())  // Emit the updated user data after removing the contact
        }.flowOn(ioDispatcher)
    }

    override suspend fun getUserContacts(uid: String): Flow<List<String>> {
        return userCollection.document(uid).snapshots()
            .map { snapshot -> snapshot.toObject(UserRemoteFirestoreModel::class.java)?.contacts ?: emptyList() }
            .flowOn(ioDispatcher)
    }
}