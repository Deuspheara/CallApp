package fr.deuspheara.callapp.ui.main

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fr.deuspheara.callapp.ui.components.snackbar.CallAppSnackBarHost
import fr.deuspheara.callapp.ui.screens.authentication.AuthNavGraph

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
){

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded

    AuthNavGraph(
        navController = navController,
        isExpandedScreen = isExpandedScreen,
        snackbarHostState = snackbarHostState
    )
}