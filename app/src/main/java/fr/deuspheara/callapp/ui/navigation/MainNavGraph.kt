package fr.deuspheara.callapp.ui.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import fr.deuspheara.callapp.ui.navigation.CallAppDestination.Companion.composable
import fr.deuspheara.callapp.ui.navigation.CallAppRoutable.Companion.navigate
import fr.deuspheara.callapp.ui.screens.authentication.reset.ResetPasswordScreen
import fr.deuspheara.callapp.ui.screens.authentication.signin.SignInScreen
import fr.deuspheara.callapp.ui.screens.home.HomeScreen

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.navigation.MainNavGraph
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Main nav graph
 *
 */
fun NavGraphBuilder.addMainNavGraph(
    isExpandedScreen: Boolean,
    navController: NavHostController,
    startDestination: CallAppDestination = CallAppDestination.Home,
    snackbarHostState: SnackbarHostState,
) {
    navigation(
        route = "Main",
        startDestination = startDestination.route,
    ) {
        composable(CallAppDestination.Home) {
            HomeScreen(
                destination = CallAppDestination.Home,
                snackbarHostState = snackbarHostState,
                navigateToSignInScreen = {
                    navController.navigate(CallAppDestination.SignIn)
                },
                navigateToSignUpScreen = {
                    navController.navigate(CallAppDestination.SignUp)
                }
            )
        }


    }

}