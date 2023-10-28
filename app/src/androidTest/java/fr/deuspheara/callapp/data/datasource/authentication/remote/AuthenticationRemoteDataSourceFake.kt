package fr.deuspheara.callapp.data.datasource.authentication.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.Instant
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.datasource.authentication.remote.AuthenticationRemoteDataSourceFake
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Authentication fake remote datasource
 *
 */
class AuthenticationRemoteDataSourceFake @Inject constructor() : AuthenticationRemoteDataSource {
    override suspend fun signUpWithPassword(email: String, password: String): Flow<String> {
        return flowOf("test")
    }

    override suspend fun signInWithPassword(email: String, password: String): Flow<String> {
        return flowOf("test")
    }

    override suspend fun isUserAuthenticated(): Flow<Boolean> {
        return flowOf(true)
    }

    override suspend fun sendPasswordResetEmail(email: String): Flow<Boolean> {
        return flowOf(true)
    }

    override suspend fun confirmResetPassword(oobCode: String, password: String): Flow<Boolean> {
        return flowOf(true)
    }

    override suspend fun checkActionCode(code: String): Flow<Boolean> {
        return flowOf(true)
    }

    override suspend fun resetPassword(password: String): Flow<Instant?> {
        return flowOf(Instant.now())
    }

}