package fr.deuspheara.callapp.ui.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import fr.deuspheara.callapp.ui.navigation.CallAppDestination.Companion.composable
import fr.deuspheara.callapp.ui.navigation.CallAppRoutable.Companion.navigate
import fr.deuspheara.callapp.ui.screens.authentication.register.SignUpScreen
import fr.deuspheara.callapp.ui.screens.authentication.reset.ResetPasswordScreen
import fr.deuspheara.callapp.ui.screens.authentication.send_reset.SendResetScreen
import fr.deuspheara.callapp.ui.screens.authentication.signin.SignInScreen

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.navigation.CallAppNavGraph
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Navigation graph
 *
 */

fun NavGraphBuilder.addAuthNavGraph(
    isExpandedScreen: Boolean,
    navController: NavHostController,
    startDestination: CallAppDestination = CallAppDestination.SignIn,
    snackbarHostState: SnackbarHostState,
    launchClientMail: () -> Unit = {},
) {
    navigation(
        route = "Auth",
        startDestination = startDestination.route,
    ) {
        composable(CallAppDestination.SignIn) {
            SignInScreen(
                snackbarHostState = snackbarHostState,
                showTopAppBar = CallAppDestination.SignIn.showTopAppBar,
                navigateBack = navController::popBackStack,
                navigateToForgetPassword = {
                    navController.navigate(CallAppDestination.SendResetPassword)
                },
            )
        }

        composable(CallAppDestination.SignUp) {
            SignUpScreen(
                navigateToSignInScreen = {
                    navController.navigate(CallAppDestination.SignIn)
                },
                navigateBack = navController::popBackStack
            )
        }

        composable(CallAppDestination.SendResetPassword) {
            SendResetScreen(
                launchClientMail = launchClientMail,
                onNavigateBack = navController::popBackStack
            )
        }

        composable(CallAppDestination.ResetPassword) {
            ResetPasswordScreen(
                onNavigateToLogin = {

                    navController.navigate(CallAppDestination.SignIn) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = false
                            saveState = false
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateBack = navController::popBackStack
            )
        }

        composable(CallAppDestination.ResetPassword) {
            ResetPasswordScreen(
                onNavigateToLogin = {

                    navController.navigate(CallAppDestination.SignIn) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = false
                            saveState = false
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateBack = navController::popBackStack
            )
        }


    }

}