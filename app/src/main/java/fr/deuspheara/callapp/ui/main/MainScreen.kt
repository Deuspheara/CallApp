package fr.deuspheara.callapp.ui.main

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fr.deuspheara.callapp.ui.navigation.CallAppDestination
import fr.deuspheara.callapp.ui.navigation.CallAppDestination.Companion.composable
import fr.deuspheara.callapp.ui.navigation.CallAppRoutable.Companion.navigate
import fr.deuspheara.callapp.ui.navigation.addAuthNavGraph
import fr.deuspheara.callapp.ui.navigation.addMainNavGraph
import fr.deuspheara.callapp.ui.screens.authentication.reset.ResetPasswordScreen

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.main.MainScreen
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Main screen
 *
 */
@Composable
fun MainScreen(
    widthSizeClass: WindowWidthSizeClass,
    navController: NavHostController = rememberNavController(),
    launchClientMail: () -> Unit = {},
    startDestination: CallAppDestination = CallAppDestination.Main,
){
    val snackbarHostState = remember { SnackbarHostState() }

    val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded

    NavHost(
        navController = navController,
        startDestination = startDestination.route,
    ) {
        addAuthNavGraph(
            navController = navController,
            isExpandedScreen = isExpandedScreen,
            snackbarHostState = snackbarHostState,
            launchClientMail = launchClientMail,
            startDestination = CallAppDestination.SignUp
        )
        addMainNavGraph(
            navController = navController,
            isExpandedScreen = isExpandedScreen,
            snackbarHostState = snackbarHostState,
            startDestination = CallAppDestination.Welcome
        )
    }


}