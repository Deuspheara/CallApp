package fr.deuspheara.callapp.ui.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import fr.deuspheara.callapp.ui.navigation.CallAppDestination.Companion.composable
import fr.deuspheara.callapp.ui.navigation.CallAppRoutable.Companion.navigate
import fr.deuspheara.callapp.ui.screens.profil.ProfilScreen
import fr.deuspheara.callapp.ui.screens.authentication.reset.ResetPasswordScreen
import fr.deuspheara.callapp.ui.screens.authentication.signin.SignInScreen
import fr.deuspheara.callapp.ui.screens.channels.ChannelsScreen
import fr.deuspheara.callapp.ui.screens.channels.ChannelsScreenContent
import fr.deuspheara.callapp.ui.screens.home.HomeScreen
import fr.deuspheara.callapp.ui.screens.profil.edit.EditProfileScreen
import fr.deuspheara.callapp.ui.screens.video.VideoScreen
import fr.deuspheara.callapp.ui.screens.welcome.WelcomeScreen
import fr.deuspheara.callapp.ui.screens.welcome.WelcomeViewModel

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

        composable(CallAppDestination.Welcome) {
            WelcomeScreen(
                snackbarHostState = snackbarHostState,
                onNavigateToProfile = { identifier ->
                    navController.navigate(CallAppDestination.Profile(identifier))
                },
                onNavigateSignUpScreen = {
                    navController.navigate(CallAppDestination.SignUp)
                },
                navController = navController,
            )
        }

        composable(CallAppDestination.Profile){
            ProfilScreen(
                onNavigateBack = navController::popBackStack,
                onNavigateToEditProfile = {
                    navController.navigate(CallAppDestination.EditProfile)
                }
            )
        }

        composable(CallAppDestination.EditProfile){
            EditProfileScreen(
                onNavigateBack = navController::popBackStack,
                snackbarHostState = snackbarHostState,
            )
        }

        composable(CallAppDestination.VideoChannel){
            ChannelsScreen(
                navController = navController,
                snackbarHostState = snackbarHostState,
                navigateToVideo = { channelName ->
                    navController.navigate(CallAppDestination.Video(channelName))
                }
            )
        }

        composable(CallAppDestination.Video){
            VideoScreen()
        }

    }

}