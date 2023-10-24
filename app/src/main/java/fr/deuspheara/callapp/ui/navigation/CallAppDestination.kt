package fr.deuspheara.callapp.ui.navigation

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
import androidx.navigation.compose.composable
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

    }

    object Home : CallAppDestination(
        title = R.string.home,
        showTopAppBar = true,
        navigationIcon = R.drawable.ic_back,
        transitionConfig = TransitionConfig(
            enter = Transition.SlideHorizontal,
            exit = Transition.Fade
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
            exit = Transition.Fade
        )
    ) {
        override val route: String
            get() = "signin_page"
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