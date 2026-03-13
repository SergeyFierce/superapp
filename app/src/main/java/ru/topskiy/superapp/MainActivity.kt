package ru.topskiy.superapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import ru.topskiy.superapp.core.navigation.ServiceNavigator
import ru.topskiy.superapp.core.services.ServiceRegistry
import ru.topskiy.superapp.core.ui.theme.AppTheme
import ru.topskiy.superapp.shell.AppRoot
import ru.topskiy.superapp.shell.ThemeViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var serviceRegistry: ServiceRegistry
    @Inject lateinit var serviceNavigator: ServiceNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val theme by themeViewModel.theme.collectAsStateWithLifecycle(
                initialValue = ru.topskiy.superapp.core.common.AppTheme.SYSTEM,
            )
            AppTheme(themeSelection = theme) {
                AppRoot(
                    serviceRegistry = serviceRegistry,
                    serviceNavigator = serviceNavigator,
                )
            }
        }
    }
}
