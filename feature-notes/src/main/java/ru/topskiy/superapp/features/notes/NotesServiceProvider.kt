package ru.topskiy.superapp.features.notes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.topskiy.superapp.core.navigation.AppRoutes
import ru.topskiy.superapp.core.services.ServiceCapability
import ru.topskiy.superapp.core.services.ServiceCategory
import ru.topskiy.superapp.core.services.ServiceDescriptor
import ru.topskiy.superapp.core.services.ServiceId
import ru.topskiy.superapp.core.services.NavigableServiceProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesServiceProvider @Inject constructor() : NavigableServiceProvider {

    override val descriptor = ServiceDescriptor(
        id = NOTES_ID,
        title = "Заметки",
        description = "Быстрые заметки и записи",
        iconKey = "notes",
        rootRoute = AppRoutes.service(NOTES_ID),
        category = ServiceCategory.PRODUCTIVITY,
        enabledByDefault = true,
        capabilities = setOf(
            ServiceCapability.HOME_BLOCK,
            ServiceCapability.SEARCH_PROVIDER,
        ),
    )

    override fun NavGraphBuilder.registerGraph(navController: NavController) {
        composable(AppRoutes.service(NOTES_ID)) {
            NotesScreen()
        }
    }
}
