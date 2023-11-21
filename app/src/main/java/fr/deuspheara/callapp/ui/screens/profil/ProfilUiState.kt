package fr.deuspheara.callapp.ui.screens.profil

import fr.deuspheara.callapp.core.model.common.Consumable
import fr.deuspheara.callapp.core.model.user.UserFullModel
import fr.deuspheara.callapp.data.datasource.user.model.UserPublicModel
import fr.deuspheara.callapp.ui.screens.authentication.reset.ResetPasswordUiState
import java.time.Instant

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.profil.ProfilUiState
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Ui state of [ProfilScreen]
 *
 */
sealed interface ProfilUiState {
    @JvmInline
    value class Loading(val isLoading: Boolean = false) : ProfilUiState
    data class Error(val exception: Throwable) : ProfilUiState
    
    class Success(val userPublicModel: UserPublicModel?) : ProfilUiState, Consumable()
    class SuccessLocal(val userFullModel: UserFullModel) : ProfilUiState, Consumable()
}