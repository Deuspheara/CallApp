package fr.deuspheara.callapp.ui.screens.welcome

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.screens.welcome.WelcomeViewModel
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * ViewModel for [WelcomeScreen]
 *
 */

@HiltViewModel
class WelcomeViewModel @Inject constructor() : ViewModel() {
    private companion object {
        private const val TAG = "WelcomeViewModel"
    }
}