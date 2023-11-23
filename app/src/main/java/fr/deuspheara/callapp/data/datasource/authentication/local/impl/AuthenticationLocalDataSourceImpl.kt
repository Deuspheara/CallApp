package fr.deuspheara.callapp.data.datasource.authentication.local.impl

import android.util.Log
import androidx.room.withTransaction
import fr.deuspheara.callapp.core.coroutine.DispatcherModule
import fr.deuspheara.callapp.core.model.text.Identifier
import fr.deuspheara.callapp.data.database.model.LocalUserEntity
import fr.deuspheara.callapp.data.database.room.CallAppDatabase
import fr.deuspheara.callapp.data.datasource.authentication.local.AuthenticationLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.datasource.authentication.local.impl.AutenticationLocalDataSourceImpl
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Implementation of [AuthenticationLocalDataSource]
 *
 */
class AuthenticationLocalDataSourceImpl @Inject constructor(
    private val database: CallAppDatabase,
    @DispatcherModule.IoDispatcher private val ioContext: CoroutineDispatcher,
) : AuthenticationLocalDataSource {

    private companion object {
        private const val TAG = "AuthLocalDataSourceImpl"
    }
    override suspend fun insertUser(entity: LocalUserEntity): Flow<Long> = channelFlow {
        launch {
            try {
                val existingUser = database.withTransaction {
                    database.userDao.getUserByEmail(entity.email)
                }

                if (existingUser == null) {
                    Log.d(TAG, "insertUser: $entity")
                    val id = database.withTransaction {
                        database.userDao.insertUser(entity)
                    }
                    send(id)
                } else {
                    send(existingUser.localId) // Assuming localId is the unique identifier
                }

                close() // Close the channel after emitting the result
            } catch (e: Exception) {
                Log.e(
                    TAG,
                    "Error while inserting user in database with user: ${entity.email}",
                    e
                )
                close(e) // Close the channel with an error if an exception occurs
            }
        }
    }.flowOn(ioContext)




    override fun getUserByEmail(email: String): Flow<LocalUserEntity?> = channelFlow {
        launch {
            try {
                val user = database.withTransaction {
                    database.userDao.getUserByEmail(email)
                }
                send(user)
                close()
            } catch (e: Exception) {
                Log.e(TAG, "Error while getting user with email: $email", e)
                close(e)
            }
        }
    }.flowOn(ioContext)
    override fun getUserByUid(uid: String): Flow<LocalUserEntity?> = channelFlow {
        launch {
            try {
                val user = database.withTransaction {
                    database.userDao.getUserByUid(uid)
                }
                Log.d(TAG, "getUserByUid: $user, for uid: $uid")
                send(user)
                close()
            } catch (e: Exception) {
                Log.e(TAG, "Error while getting user with uid: $uid", e)
                close(e)
            }
        }
    }.flowOn(ioContext)

    override suspend fun deleteUserWithUid(uid: String): Flow<Int> = channelFlow {
        launch {
            try {
                val user = database.withTransaction {
                    database.userDao.deleteUserWithUid(uid)
                }
                send(user)
                close()
            } catch (e: Exception) {
                Log.e(TAG, "Error while deleting user with uid: $uid", e)
                close(e)
            }
        }
    }.flowOn(ioContext)
    override suspend fun updateUserWithUid(
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
    ): Flow<Int> = channelFlow {
        launch {
            try {
                val user = database.withTransaction {
                    database.userDao.updateUserWithUid(
                        uid,
                        identifier?.value,
                        displayName,
                        firstname,
                        lastname,
                        photoUrl,
                        email,
                        phoneNumber,
                        isEmailVerified,
                        bio,
                        contactList,
                        providerId
                    )
                }
                Log.d(TAG, "updateUserWithUid: $user")
                send(user)
                close()
            } catch (e: Exception) {
                Log.e(TAG, "Error while updating user with uid: $uid", e)
                close(e)
            }
        }
    }.flowOn(ioContext)

    override suspend fun insertOrUpdateUser(localUserEntity: LocalUserEntity): Flow<Long> = channelFlow {
        launch {
            try {
                val existingUser = database.withTransaction {
                    database.userDao.getUserByEmail(localUserEntity.email)
                }

                if (existingUser == null) {
                    val id = database.withTransaction {
                        database.userDao.insertUser(localUserEntity)
                    }
                    send(id)
                } else {
                    send(existingUser.localId) // Assuming localId is the unique identifier
                }

                close() // Close the channel after emitting the result
            } catch (e: Exception) {
                Log.e(
                    TAG,
                    "Error while inserting user in database with user: ${localUserEntity.email}",
                    e
                )
                close(e) // Close the channel with an error if an exception occurs
            }
        }
    }.flowOn(ioContext)

    override suspend fun deleteUser(): Flow<Int> = channelFlow {
        launch {
            try {
                val user = database.withTransaction {
                    database.userDao.deleteAllUsers()
                }
                send(user)
                close()
            } catch (e: Exception) {
                Log.e(TAG, "Error while deleting user", e)
                close(e)
            }
        }
    }.flowOn(ioContext)
}