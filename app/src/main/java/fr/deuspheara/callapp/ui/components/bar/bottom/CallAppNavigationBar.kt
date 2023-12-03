package fr.deuspheara.callapp.ui.components.bar.bottom

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import fr.deuspheara.callapp.ui.navigation.CallAppDestination

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.components.bar.bottom.CallAppNavigationBar
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Call app navigation bar
 *
 */
@Composable
fun CallAppNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,

        ) {
        CallAppDestination.bottomBarItems.forEach {
            NavigationBarItem(
                selected = navController.currentDestination?.route == it.route,
                onClick = {
                    navController.navigate(it.route) {
                        popUpTo(CallAppDestination.Main.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    it.navigationIcon?.let { icon ->
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = stringResource(id = it.title),
                        )
                    }
                },
                label = {
                    Text(
                        text = stringResource(id = it.title),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            )
        }
    }
}