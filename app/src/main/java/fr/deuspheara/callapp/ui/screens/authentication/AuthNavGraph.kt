package fr.deuspheara.callapp.ui.screens.authentication

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import fr.deuspheara.callapp.ui.navigation.CallAppDestination
import fr.deuspheara.callapp.ui.screens.home.HomeScreen
import fr.deuspheara.callapp.ui.navigation.CallAppDestination.Companion.composable
import fr.deuspheara.callapp.ui.screens.authentication.signin.SignInScreen
import fr.deuspheara.callapp.ui.navigation.CallAppRoutable.Companion.navigate
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

@Composable
fun AuthNavGraph(
    modifier: Modifier = Modifier,
    isExpandedScreen : Boolean,
    navController: NavHostController = rememberNavController(),
    startDestination: CallAppDestination = CallAppDestination.Home,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
        modifier = modifier
    ) {
        composable(CallAppDestination.Home){
            HomeScreen(
                destination = CallAppDestination.Home,
                snackbarHostState = snackbarHostState,
                navigateToSignInScreen = {
                    navController.navigate(CallAppDestination.SignIn)
                }
            )
        }
        composable(CallAppDestination.SignIn){
            SignInScreen(
                snackbarHostState = snackbarHostState,
                showTopAppBar = CallAppDestination.SignIn.showTopAppBar,
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}