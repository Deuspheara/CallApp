package fr.deuspheara.callapp.data.datasource.authentication.remote.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import fr.deuspheara.callapp.core.coroutine.DispatcherModule
import fr.deuspheara.callapp.data.datasource.authentication.remote.AuthenticationRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.time.Instant
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.datasource.authentication.remote.impl.AuthenticationRemoteDataSourceImpl
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Implementation of [AuthenticationRemoteDataSource]
 *
 */
class AuthenticationRemoteDataSourceImpl @Inject constructor(
    @DispatcherModule.IoDispatcher private val ioContext: CoroutineDispatcher,
    private val firebaseAuth: FirebaseAuth,
) : AuthenticationRemoteDataSource {
    override suspend fun signUpWithPassword(email: String, password: String): Flow<String> {
        return flow {
            val uid = firebaseAuth.createUserWithEmailAndPassword(email, password).await().user?.uid
            uid?.let { emit(it) }
        }.catch {
            if (it is FirebaseAuthUserCollisionException) throw IllegalStateException("User collision occurred.")
        }.flowOn(ioContext)
    }

    override suspend fun signInWithPassword(email: String, password: String): Flow<String> {
        return flow {
            val uid = firebaseAuth.signInWithEmailAndPassword(email, password).await().user?.uid
            uid?.let { emit(it) } ?: throw IllegalStateException("Authentication failed: No user returned.")
        }.catch {
            when (it) {
                is FirebaseAuthInvalidCredentialsException -> throw IllegalStateException("Invalid credentials for $email")
                is FirebaseAuthInvalidUserException -> throw IllegalStateException("No user found for $email")
                else -> throw it
            }
        }.flowOn(ioContext)
    }

    override suspend fun isUserAuthenticated(): Flow<Boolean> {
        return flow {
            val isAuthenticated = firebaseAuth.currentUser?.getIdToken(true)?.await()?.token != null
            emit(isAuthenticated)
            if (!isAuthenticated) firebaseAuth.signOut()
        }.catch {
            firebaseAuth.signOut()
            emit(false)
        }.flowOn(ioContext)
    }


}
