package fr.deuspheara.callapp.data.repository.authentication.impl

import android.util.Log
import fr.deuspheara.callapp.core.coroutine.DispatcherModule
import fr.deuspheara.callapp.core.model.user.UserFullModel
import fr.deuspheara.callapp.data.database.model.LocalUserEntity
import fr.deuspheara.callapp.data.datasource.authentication.local.AuthenticationLocalDataSource
import fr.deuspheara.callapp.data.datasource.authentication.remote.AuthenticationRemoteDataSource
import fr.deuspheara.callapp.data.datasource.user.model.UserRemoteModel
import fr.deuspheara.callapp.data.repository.authentication.AuthenticationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.repository.authentication.impl.AuthenticationRepositoryImpl
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Implementation of [AuthenticationRepository]
 *
 */
class AuthenticationRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthenticationRemoteDataSource,
    private val localDataSource: AuthenticationLocalDataSource,
    @DispatcherModule.DefaultDispatcher private val defaultContext: CoroutineDispatcher
) : AuthenticationRepository {

    private companion object {
        private const val TAG = "AuthenticationRepositoryImpl"
    }

    override suspend fun signUpWithPassword(email: String, password: String): Flow<UserRemoteModel> =
        withContext(defaultContext) {
            val userRemoteModel = remoteDataSource.signUpWithPassword(
                email = email,
                password = password
            )

            userRemoteModel
        }

    override suspend fun signInWithPassword(email: String, password: String): Flow<UserRemoteModel> =
        withContext(defaultContext) {
            val userRemoteModel = remoteDataSource.signInWithPassword(email, password)

            userRemoteModel
        }

    override suspend fun isUserAuthenticated(): Flow<Boolean> = withContext(defaultContext) {
        remoteDataSource.isUserAuthenticated()
    }

    override suspend fun sendPasswordResetEmail(email: String): Flow<Boolean> =
        withContext(defaultContext) {
            remoteDataSource.sendPasswordResetEmail(email)
        }

    override suspend fun confirmResetPassword(oobCode: String, password: String): Flow<Boolean> =
        withContext(defaultContext) {
            remoteDataSource.confirmResetPassword(oobCode, password)
        }

    override suspend fun checkActionCode(code: String): Flow<Boolean> =
        withContext(defaultContext) {
            remoteDataSource.checkActionCode(code)
        }

    override suspend fun resetPassword(password: String): Flow<Instant?> {
        return withContext(defaultContext) {
            remoteDataSource.resetPassword(password)
        }
    }

    override suspend fun signOut(): Flow<String?> {
        return withContext(defaultContext) {
            localDataSource.deleteUser()
            remoteDataSource.signOut()

        }
    }

    override suspend fun getCurrentUser(): Flow<UserFullModel?> {
        return withContext(defaultContext) {
            remoteDataSource.getCurrentUser().map { it?.toUserFullModel() }
        }
    }

    override suspend fun getCurrentLocalUser(): Flow<UserFullModel?> {
        return flow {
            val uid = remoteDataSource.getCurrentUser().firstOrNull()?.uuid
            Log.d(TAG, "getCurrentLocalUser: $uid")

            if (uid != null) {
                val localUser = localDataSource.getUserByUid(uid).firstOrNull()
                emit(localUser?.toUserFullModel())
            }
        }.flowOn(defaultContext)
    }


    private fun UserRemoteModel.toUserFullModel(): UserFullModel {
        return UserFullModel(
            uid = uuid,
            displayName = displayName,
            firstName = firstName,
            lastName = lastName,
            email = email,
            photoUrl = photoUrl,
            bio = "",
            phoneNumber = phoneNumber,
            identifier = "",
            contactList = emptyList()
        )
    }

    private fun LocalUserEntity.toUserFullModel(): UserFullModel {
        return UserFullModel(
            uid = fireStoreUUID,
            displayName = displayName,
            firstName = firstName,
            lastName = lastName,
            email = email,
            photoUrl = photoUrl,
            bio = bio,
            phoneNumber = phoneNumber,
            identifier = identifier,
            contactList = emptyList()
        )
    }

    private fun UserRemoteModel.toLocalUserEntity(): LocalUserEntity {
        return LocalUserEntity(
            fireStoreUUID = uuid,
            displayName = displayName,
            firstName = firstName,
            lastName = lastName,
            email = email,
            photoUrl = photoUrl,
            bio = "",
            phoneNumber = phoneNumber,
            identifier = "",
            isEmailVerified = isEmailVerified,
            providerId = providerId,
            contactList = emptyList()
        )
    }

}