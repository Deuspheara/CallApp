package fr.deuspheara.callapp.ui.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import fr.deuspheara.callapp.ui.screens.home.HomeScreen
import fr.deuspheara.callapp.ui.navigation.CallAppDestination.Companion.composable

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
fun CallAppNavGraph(
    isExpandedScreen : Boolean,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    startDestination: CallAppDestination = CallAppDestination.Home,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
        modifier = modifier
    ) {
        composable(CallAppDestination.Home){
            HomeScreen(
                destination = CallAppDestination.Home,
                snackbarHostState = SnackbarHostState()
            )
        }
    }
}