package fr.deuspheara.callapp.domain.authentication

import android.util.Log
import fr.deuspheara.callapp.core.model.user.UserFullModel
import fr.deuspheara.callapp.data.repository.authentication.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.domain.authentication.GetCurrentLocalUserUseCase
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Get current local user
 *
 */
class GetCurrentLocalUserUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
) {
    private companion object {
        private const val TAG = "GetCurrentLocalUserUseCase"
    }
    suspend operator fun invoke() : Flow<UserFullModel?> {
     return  authenticationRepository.getCurrentUser()
         .flatMapLatest { user ->
             user?.let {
                 authenticationRepository.getUserByUid(it.uid)
             } ?: flowOf(null)
         }
         .catch { e ->
             Log.e(TAG, "Error while getting current user", e)
             emit(null)
         }
    }


}