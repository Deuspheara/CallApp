package fr.deuspheara.callapp.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.navigation.CallAppRoutable
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Represents a routable destination within the Call App.
 *
 * Defines a consistent mechanism for navigation and offers utility functions for more streamlined navigation
 * using the [NavController]. All destinations that are meant to be navigated to within the app should
 * implement this interface to ensure a consistent navigation scheme.
 *
 * @property route A unique string identifier representing the navigation target.
 */
sealed interface CallAppRoutable {

    val route: String

    companion object {

        /**
         * Navigates to the provided routable destination.
         *
         * @param routable The destination to navigate to, implementing the [CallAppRoutable] interface.
         * @param navOptions Optional navigation options to apply for this navigation action.
         */
        fun NavController.navigate(
            routable: CallAppRoutable,
            navOptions: NavOptions? = null
        ) = navigate(routable.route, navOptions)

        /**
         * Navigates to the provided routable destination with a custom set of navigation options.
         *
         * @param routable The destination to navigate to, implementing the [CallAppRoutable] interface.
         * @param builder A lambda allowing customization of navigation options using [NavOptionsBuilder].
         */
        fun NavController.navigate(
            routable: CallAppRoutable,
            builder: NavOptionsBuilder.() -> Unit
        ) = navigate(routable.route, navOptions(builder))

    }
}