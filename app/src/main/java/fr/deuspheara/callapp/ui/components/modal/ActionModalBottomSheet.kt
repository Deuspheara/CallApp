package fr.deuspheara.callapp.ui.components.modal

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.deuspheara.callapp.R
import fr.deuspheara.callapp.ui.theme.CallAppTheme

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.components.modal.ActionModalBottomSheet
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Action modal bottom sheet
 *
 */
@Composable
fun ActionModalBottomSheet(
    onDismissRequest: () -> Unit,
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it == SheetValue.Expanded }
    ),
    modalHeight: Dp = 200.dp,
    content: @Composable () -> Unit = {}
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 36.dp),
        tonalElevation = BottomSheetDefaults.Elevation
    ) {
        Column(
            modifier = Modifier
                .heightIn(modalHeight)
                .verticalScroll(state = rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = title),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.requiredHeight(8.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) { content() }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ActionModalBottomSheetPreview() {
    CallAppTheme {
        Surface(
            modifier = Modifier.size(50.dp)
        ) {
            ActionModalBottomSheet(
                onDismissRequest = { },
                title = R.string.signup,
                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true,
                    confirmValueChange = { it == SheetValue.Expanded }
                ),
            ) {

            }
        }
    }

}