package fr.deuspheara.callapp.data.datasource.authentication.remote.impl

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import fr.deuspheara.callapp.core.coroutine.DispatcherModule
import fr.deuspheara.callapp.data.datasource.authentication.remote.AuthenticationRemoteDataSource
import fr.deuspheara.callapp.data.datasource.user.model.UserRemoteModel
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
    private companion object {
        private const val TAG = "AuthenticationRemoteDataSourceImpl"
    }

    override suspend fun signUpWithPassword(email: String, password: String): Flow<UserRemoteModel> {
        return flow {
            val user = firebaseAuth.createUserWithEmailAndPassword(email, password).await().user
            user?.let { emit(UserRemoteModel(it)) }
                ?: throw IllegalStateException("Authentication failed: No user returned.")
        }.catch {
            Log.e(TAG, "signUpWithPassword: ", it)
            if (it is FirebaseAuthUserCollisionException) throw IllegalStateException("User collision occurred.")
        }.flowOn(ioContext)
    }

    override suspend fun signInWithPassword(email: String, password: String): Flow<UserRemoteModel> {
        return flow {
            val user = firebaseAuth.signInWithEmailAndPassword(email, password).await().user
            user?.let { emit(UserRemoteModel(it)) }
                ?: throw IllegalStateException("Authentication failed: No user returned.")
        }.catch {
            Log.e(TAG, "signInWithPassword: ", it)
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
            Log.e(TAG, "isUserAuthenticated: ", it)
            firebaseAuth.signOut()
            emit(false)
        }.flowOn(ioContext)
    }

    override suspend fun sendPasswordResetEmail(email: String): Flow<Boolean> {
        return flow {
            firebaseAuth.sendPasswordResetEmail(email).await()
            emit(true)
        }.catch {
            Log.e(TAG, "sendPasswordResetEmail: ", it)
            emit(false)
        }.flowOn(ioContext)
    }

    override suspend fun confirmResetPassword(oobCode: String, password: String): Flow<Boolean> {
        return flow {
            firebaseAuth.confirmPasswordReset(oobCode, password).await()
            emit(true)
        }.catch {
            Log.e(TAG, "confirmResetPassword: ", it)
            emit(false)
        }.flowOn(ioContext)
    }

    override suspend fun checkActionCode(code: String): Flow<Boolean> {
        return flow {
            firebaseAuth.checkActionCode(code).await()
            emit(true)
        }.catch {
            Log.e(TAG, "checkActionCode: ", it)
            emit(false)
        }.flowOn(ioContext)
    }

    override suspend fun resetPassword(password: String): Flow<Instant?> {
        return flow {
            val user = firebaseAuth.currentUser
            user?.updatePassword(password)?.await()
            emit(Instant.now())
        }.catch {
            Log.e(TAG, "resetPassword: ", it)
            emit(null)
        }.flowOn(ioContext)
    }

    override suspend fun signOut(): Flow<String?> {
        return flow {
            val uid = firebaseAuth.currentUser?.uid
            firebaseAuth.signOut()
            emit(uid)
        }.catch {
            Log.e(TAG, "signOut: ", it)
            emit(null)
        }.flowOn(ioContext)
    }

    override suspend fun getCurrentUser(): Flow<UserRemoteModel?> {
        return flow {
            val user = firebaseAuth.currentUser
            user?.reload()?.await()

            emit(user?.let { UserRemoteModel(it) })
        }.catch {
            Log.e(TAG, "getCurrentUser: ", it)
            emit(UserRemoteModel())
        }.flowOn(ioContext)
    }
}


