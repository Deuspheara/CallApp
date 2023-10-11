package fr.deuspheara.callapp.ui.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import fr.deuspheara.callapp.ui.home.HomeScreen
import fr.deuspheara.callapp.ui.navigation.CallAppDestination.Companion.composable
import org.openjdk.tools.javac.Main

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
    startDestination: CallAppDestination = CallAppDestination.HomePage,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
        modifier = modifier
    ) {
        composable(CallAppDestination.HomePage){
            HomeScreen(
                destination = CallAppDestination.HomePage,
                snackbarHostState = snackbarHostState
            )
        }
    }
}