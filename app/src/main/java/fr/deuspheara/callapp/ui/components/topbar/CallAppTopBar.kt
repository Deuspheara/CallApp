package fr.deuspheara.callapp.ui.components.topbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import fr.deuspheara.callapp.ui.navigation.CallAppDestination

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.components.topbar.CallAppTopBar
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Call app top bar
 *
 */
@Composable
fun CallAppTopBar(
    modifier: Modifier = Modifier,
    destination: CallAppDestination,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        title =
        {
            Text(
                text = stringResource(destination.title),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondary,
            )
        },
        navigationIcon = navigationIcon,
        actions = actions,
        modifier = modifier
    )
}