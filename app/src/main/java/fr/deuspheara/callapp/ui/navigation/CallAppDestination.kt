package fr.deuspheara.callapp.ui.navigation

import android.telecom.Call
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import fr.deuspheara.callapp.R

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.navigation.CallAppDestination
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Represents the destination configurations within the Call App.
 *
 * @param title Resource ID for the title displayed on this destination.
 * @param showTopAppBar Flag indicating if the top app bar should be visible on this destination. Useful for screens that require more real estate or a distraction-free layout.
 * @param navigationIcon Optional resource ID for a navigation icon. Null if no icon should be shown.
 * @param arguments Navigation arguments specific to this destination. Useful for parameterized navigation.
 * @param deepLinks Deep link intents that map to this destination. Allows external or internal app components to navigate directly.
 * @param transitionConfig Defines the visual transitions (entering, exiting) for this destination. Useful for overriding default animations or adding custom transitions.
 */
sealed class CallAppDestination(
    @StringRes val title: Int,
    val showTopAppBar: Boolean = true,
    val navigationIcon: Int? = null,
    val arguments: List<NamedNavArgument> = emptyList(),
    val deepLinks: List<NavDeepLink> = emptyList(),
    val transitionConfig: TransitionConfig = TransitionConfig()
) : CallAppRoutable {

    companion object {
        val bottomBarItems: List<CallAppDestination>
            get() = listOf(Home)

        private fun getEnterTransition(transition: Transition): EnterTransition {
            return when (transition) {
                Transition.Default -> fadeIn()
                Transition.SlideHorizontal -> slideInHorizontally { it }
                Transition.Fade -> fadeIn()
            }
        }

        private fun getExitTransition(transition: Transition): ExitTransition {
            return when (transition) {
                Transition.Default -> fadeOut()
                Transition.SlideHorizontal -> slideOutHorizontally { it }
                Transition.Fade -> fadeOut()
            }
        }

        fun NavGraphBuilder.composable(
            destination: CallAppDestination,
            content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit,
        ) {
            this.composable(
                route = destination.route,
                deepLinks = destination.deepLinks,
                arguments = destination.arguments,
                content = content,
                enterTransition = { getEnterTransition(destination.transitionConfig.enter) },
                exitTransition = {  getExitTransition(destination.transitionConfig.exit) },
                popEnterTransition = {  getEnterTransition(destination.transitionConfig.popEnter) },
                popExitTransition = { getExitTransition(destination.transitionConfig.popExit) }
            )
        }

        fun getByRoute(route: String): CallAppDestination? {
            return when {
                route.startsWith(Home.route) -> Home
                route.startsWith(SignIn.route) -> SignIn
                route.startsWith(SignUp.route) -> SignUp
                route.startsWith(SendResetPassword.route) -> SendResetPassword
                route.startsWith(ResetPassword.route) -> ResetPassword
                else -> null
            }
        }
    }

    class ResetPassword(oobCode: String?) : CallAppRoutable {
        override val route: String =
            "${ROUTING_PREFIX}${if (oobCode != null) "?${ARG_KEY_CODE}=${oobCode}" else ""}"

        companion object : CallAppDestination(
            title = R.string.new_password,
            showTopAppBar = true,
            navigationIcon = R.drawable.ic_back,
            transitionConfig = TransitionConfig(
                enter = Transition.SlideHorizontal,
                popExit = Transition.SlideHorizontal
            ),
            arguments = listOf(
                navArgument(ResetPassword.ARG_KEY_CODE) {
                    this.type = NavType.StringType
                    this.nullable = true
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern =
                        "https://callapp-c1cd7.firebaseapp.com/__/auth/action?${ResetPassword.ARG_KEY_CODE}={${ResetPassword.ARG_KEY_CODE}}"
                }
            )
        ) {
            const val ROUTING_PREFIX = "reset_password"
            const val ARG_KEY_CODE = "oobCode"

            override val route: String = "$ROUTING_PREFIX?$ARG_KEY_CODE={$ARG_KEY_CODE}"
        }
    }

    object Home : CallAppDestination(
        title = R.string.home,
        showTopAppBar = true,
        navigationIcon = R.drawable.ic_back,
        transitionConfig = TransitionConfig(
            enter = Transition.SlideHorizontal,
            popExit = Transition.SlideHorizontal
        )
    ) {
        override val route: String
            get() = "home_page"
    }

    object SignIn : CallAppDestination(
        title = R.string.i_have_an_account,
        showTopAppBar = true,
        navigationIcon = R.drawable.ic_back,
        transitionConfig = TransitionConfig(
            enter = Transition.SlideHorizontal,
            popExit = Transition.SlideHorizontal
        )
    ) {
        override val route: String
            get() = "signin_page"
    }

    object SignUp : CallAppDestination(
        title = R.string.signup,
        showTopAppBar = true,
        navigationIcon = R.drawable.ic_back,
        transitionConfig = TransitionConfig(
            enter = Transition.SlideHorizontal,
            popExit = Transition.SlideHorizontal
        )
    ) {
        override val route: String
            get() = "signup_page"
    }

    data object SendResetPassword : CallAppDestination(
        title = R.string.forgotten_password,
        showTopAppBar = true,
        navigationIcon = R.drawable.ic_back,
        transitionConfig = TransitionConfig(
            enter = Transition.SlideHorizontal,
            popExit = Transition.SlideHorizontal
        )
    ) {
        override val route: String
            get() = "send_reset_password"

    }


   object Auth : CallAppDestination(
        title = R.string.authentication,
    ) {
        override val route: String
            get() = "auth"
    }
    object Main : CallAppDestination(
        title = R.string.main,
    ) {
        override val route: String
            get() = "Main"
    }
}


data class TransitionConfig(
    val enter: Transition = Transition.Default,
    val exit: Transition = Transition.Default,
    val popEnter: Transition = Transition.Default,
    val popExit: Transition = Transition.Default
)

enum class Transition {
    Default, SlideHorizontal, Fade
}