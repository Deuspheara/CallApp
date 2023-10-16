package fr.deuspheara.callapp.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import fr.deuspheara.callapp.ui.theme.CallAppTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.ui.main.MainActivity
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Main activity of the app
 *
 */
@AndroidEntryPoint
class MainActivity() : ComponentActivity() {

    private companion object {
        private const val TAG = "MainActivity"
    }

    private val viewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        var state by mutableStateOf<MainUiState>(MainUiState.Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest {
                    state = it
                }
            }
        }

        setContent {
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
            CallAppTheme {
                MainScreen(widthSizeClass)
            }
        }
    }

}