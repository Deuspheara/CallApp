package fr.deuspheara.callapp.data.datasource.user.remote.impl

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.snapshots
import fr.deuspheara.callapp.core.coroutine.DispatcherModule
import fr.deuspheara.callapp.data.datasource.user.model.UserRemote
import fr.deuspheara.callapp.data.datasource.user.remote.UserRemoteDataSource
import fr.deuspheara.callapp.data.firebase.FirebaseModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
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
    override fun registerUser(
        uid: String,
        pseudonym: String,
        realName: String,
        email: String,
        profilePictureUrl: String?,
        bio: String?
    ): Flow<String> {
        return flow {
            val newUserRemote = UserRemote(
                uid = uid,
                pseudonym = pseudonym,
                realName = realName,
                email = email,
                profilePictureUrl = profilePictureUrl,
                bio = bio,
                contactList = emptyList(),
            )
            userCollection.document(uid).set(newUserRemote).await()
            emit(uid)
        }.flowOn(ioDispatcher)
    }

    override fun getUserDetails(uid: String): Flow<UserRemote> {
        return userCollection.document(uid).snapshots()
            .map { snapshot -> snapshot.toObject(UserRemote::class.java)!! }
            .flowOn(ioDispatcher)
    }

    override fun updateUserDetails(
        uid: String,
        pseudonym: String?,
        realName: String?,
        email: String?,
        profilePictureUrl: String?,
        bio: String?
    ): Flow<UserRemote> {
        return flow {
            val updates = mutableMapOf<String, Any?>()
            pseudonym?.let { updates["pseudonym"] = it }
            realName?.let { updates["realName"] = it }
            email?.let { updates["email"] = it }
            profilePictureUrl?.let { updates["profilePictureUrl"] = it }
            bio?.let { updates["bio"] = it }

            userCollection.document(uid).update(updates).await()
            emit(getUserDetails(uid).first())  // Emit the updated user data after performing the update
        }.flowOn(ioDispatcher)
    }

    override fun addContactToUser(uid: String, contactUid: String): Flow<UserRemote> {
        return flow {
            val user = getUserDetails(uid).first()
            val updatedContacts = user.contactList.toMutableList().apply { add(contactUid) }
            userCollection.document(uid).update("contacts", updatedContacts).await()
            emit(getUserDetails(uid).first())  // Emit the updated user data after adding the contact
        }.flowOn(ioDispatcher)
    }

    override fun removeContactFromUser(uid: String, contactUid: String): Flow<UserRemote> {
        return flow {
            val user = getUserDetails(uid).first()
            val updatedContacts = user.contactList.toMutableList().apply { remove(contactUid) }
            userCollection.document(uid).update("contacts", updatedContacts).await()
            emit(getUserDetails(uid).first())  // Emit the updated user data after removing the contact
        }.flowOn(ioDispatcher)
    }

    override fun getUserContacts(uid: String): Flow<List<String>> {
        return userCollection.document(uid).snapshots()
            .map { snapshot -> snapshot.toObject(UserRemote::class.java)?.contactList ?: emptyList() }
            .flowOn(ioDispatcher)
    }
}