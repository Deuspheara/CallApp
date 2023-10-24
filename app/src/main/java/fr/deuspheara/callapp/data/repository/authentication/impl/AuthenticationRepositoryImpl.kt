package fr.deuspheara.callapp.data.repository.authentication.impl

import fr.deuspheara.callapp.core.coroutine.DispatcherModule
import fr.deuspheara.callapp.data.datasource.authentication.remote.AuthenticationRemoteDataSource
import fr.deuspheara.callapp.data.repository.authentication.AuthenticationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
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
    @DispatcherModule.DefaultDispatcher private val defaultContext: CoroutineDispatcher
) : AuthenticationRepository {

    override suspend fun signUpWithPassword(email: String, password: String): Flow<String> =
        withContext(defaultContext) {
            remoteDataSource.signUpWithPassword(
                email = email,
                password = password
            )
        }

    override suspend fun signInWithPassword(email: String, password: String): Flow<String> =
        withContext(defaultContext) {
            remoteDataSource.signInWithPassword(email, password)
        }

    override suspend fun isUserAuthenticated(): Flow<Boolean> = withContext(defaultContext) {
        remoteDataSource.isUserAuthenticated()
    }
}