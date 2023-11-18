package fr.deuspheara.callapp.ui.screens.welcome

import fr.deuspheara.callapp.core.model.common.Consumable
import fr.deuspheara.callapp.core.model.user.UserFullModel
import fr.deuspheara.callapp.data.datasource.user.model.UserPublicModel
import fr.deuspheara.callapp.ui.screens.profil.ProfilUiState

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.screens.welcome.WelcomeUiState
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Ui State for [WelcomeScreen]
 *
 */
sealed interface WelcomeUiState {
    @JvmInline
    value class Loading(val isLoading: Boolean = false) : WelcomeUiState

    object Success : WelcomeUiState, Consumable()

    object Profil : WelcomeUiState, Consumable()
    data class Error(val exception: Throwable) : WelcomeUiState, Consumable()
}