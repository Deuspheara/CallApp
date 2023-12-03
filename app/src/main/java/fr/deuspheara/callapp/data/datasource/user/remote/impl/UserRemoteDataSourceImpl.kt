package fr.deuspheara.callapp.data.datasource.user.remote.impl

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.snapshots
import fr.deuspheara.callapp.core.coroutine.DispatcherModule
import fr.deuspheara.callapp.core.model.text.Email
import fr.deuspheara.callapp.core.model.text.PhoneNumber
import fr.deuspheara.callapp.data.datasource.user.model.UserPublicModel
import fr.deuspheara.callapp.data.datasource.user.model.UserRemoteFirestoreModel
import fr.deuspheara.callapp.data.datasource.user.remote.UserRemoteDataSource
import fr.deuspheara.callapp.data.firebase.FirebaseModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
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
    @FirebaseModule.UserPublicCollectionReference private val userPublicCollection: CollectionReference,
    @DispatcherModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : UserRemoteDataSource {

    private companion object {
        private const val TAG = "UserRemoteDataSourceImpl"
    }

    override suspend fun registerUser(
        uid: String,
        identifier: String,
        email: String,
        displayName: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        photoUrl: String,
        bio: String
    ): Flow<String> = flow {
        val newUserRemote = UserRemoteFirestoreModel(
            uid = uid,
            identifier = identifier,
            displayName = displayName,
            firstName = firstName,
            lastName = lastName,
            email = email,
            photoUrl = photoUrl,
            bio = bio,
            phoneNumber = phoneNumber,
            contacts = emptyList(),
        )
        val newUserPublic = UserPublicModel(
            uid = uid,
            identifier = identifier,
            displayName = displayName,
            profilePictureUrl = photoUrl,
            bio = bio,
        )
        userCollection.document(uid).set(newUserRemote).await()
        userPublicCollection.document(uid).set(newUserPublic).await()
        emit(uid)
    }.catch { e ->
        Log.e(TAG, "registerUser: ", e)
        throw IllegalStateException("Error while registering user", e)
    }.flowOn(ioDispatcher)


    override suspend fun getUserDetails(uid: String): Flow<UserRemoteFirestoreModel?> =
        flow {
            Log.d(TAG, "getUserDetails: $uid")
            emit(
                userCollection.document(uid).snapshots().first()
                    .toObject(UserRemoteFirestoreModel::class.java)
            )
        }.catch { e ->
            Log.e(TAG, "getUserDetails: ", e)
            throw IllegalStateException("Error while getting user details", e)
        }.flowOn(ioDispatcher)

    override suspend fun updateUserDetails(
        uid: String,
        displayName: String?,
        firstName: String?,
        lastName: String?,
        email: Email?,
        profilePictureUrl: String?,
        bio: String?,
        phoneNumber: PhoneNumber?
    ): Flow<UserRemoteFirestoreModel?> = flow {
        val updatesPrivate = mapOf(
            "displayName" to displayName,
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to email?.value,
            "photoUrl" to profilePictureUrl,
            "bio" to bio,
            "phoneNumber" to phoneNumber?.value
        ).filterValues { it != null }

        val updatesPublic = mapOf(
            "displayName" to displayName,
            "bio" to bio
        ).filterValues { it != null }

        userCollection.document(uid).update(updatesPrivate).await()
        userPublicCollection.document(uid).update(updatesPublic).await()

        emit(getUserDetails(uid).first())
    }.catch { e ->
        Log.e(TAG, "updateUserDetails: ", e)
        throw IllegalStateException("Error while updating user details", e)
    }.flowOn(ioDispatcher)


    override suspend fun addContactToUser(
        uid: String,
        contactUid: String
    ): Flow<UserRemoteFirestoreModel?> {
        return flow {
            val user = getUserDetails(uid).first()
            val updatedContacts = user?.contacts?.toMutableList()?.apply { add(contactUid) }
            userCollection.document(uid).update("contacts", updatedContacts).await()
            emit(getUserDetails(uid).first())  // Emit the updated user data after adding the contact
        }.catch {
            Log.e(TAG, "addContactToUser: ", it)
            throw IllegalStateException("Error while adding contact to user", it)
        }.flowOn(ioDispatcher)
    }

    override suspend fun removeContactFromUser(
        uid: String,
        contactUid: String
    ): Flow<UserRemoteFirestoreModel?> {
        return flow {
            val user = getUserDetails(uid).first()
            val updatedContacts = user?.contacts?.toMutableList()?.apply { remove(contactUid) }
            userCollection.document(uid).update("contacts", updatedContacts).await()
            emit(getUserDetails(uid).first())  // Emit the updated user data after removing the contact
        }.catch { e ->
            Log.e(TAG, "removeContactFromUser: ", e)
            throw IllegalStateException("Error while removing contact from user", e)
        }.flowOn(ioDispatcher)
    }

    override suspend fun getUserContacts(uid: String): Flow<List<String>> {
        return userCollection.document(uid).snapshots()
            .map { snapshot ->
                snapshot.toObject(UserRemoteFirestoreModel::class.java)?.contacts ?: emptyList()
            }
            .catch {
                Log.e(TAG, "getUserContacts: ", it)
                throw IllegalStateException("Error while getting user contacts", it)
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun getPublicUserDetails(): Flow<List<UserPublicModel>> {
        return userPublicCollection.snapshots()
            .map { snapshot -> snapshot.toObjects(UserPublicModel::class.java) }
            .catch {
                Log.e(TAG, "getPublicUserDetails: ", it)
                throw IllegalStateException("Error while getting public user details", it)
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun getPublicUserDetails(identifier: String): Flow<UserPublicModel> = flow {

        val userDocument = userPublicCollection.whereEqualTo("identifier", identifier).get().await()
            .documents.firstOrNull()


        if (userDocument != null) {
            val userPublicModel = userDocument.toObject(UserPublicModel::class.java)
            if (userPublicModel != null) {
                emit(userPublicModel)
            } else {
                throw IllegalStateException("Error converting user document to UserPublicModel")
            }
        } else {
            emitErrorState(IllegalStateException("User document not found"))
        }

    }.catch {
        Log.e(TAG, "getPublicUserDetails: ", it)
        throw IllegalStateException("Error while getting public user details", it)
    }.flowOn(ioDispatcher)

    private fun emitErrorState(e: Throwable): UserPublicModel {
        // Handle the error state as needed
        return UserPublicModel(/* Set default values or handle accordingly */)
    }


}