package ru.topskiy.superapp.features.notes

import ru.topskiy.superapp.core.navigation.AppRoutes
import ru.topskiy.superapp.core.search.SearchProvider
import ru.topskiy.superapp.core.search.SearchResult
import ru.topskiy.superapp.core.services.ServiceId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesSearchProvider @Inject constructor() : SearchProvider {

    override val serviceId: ServiceId = NOTES_ID

    private val stubNotes = listOf(
        "Список покупок",
        "Идеи для проекта",
        "Контакты партнёров",
        "Рецепт борща",
        "Пароль от роутера",
    )

    override suspend fun search(query: String): List<SearchResult> =
        stubNotes
            .filter { it.contains(query, ignoreCase = true) }
            .mapIndexed { index, note ->
                SearchResult(
                    serviceId = NOTES_ID,
                    id = "note_$index",
                    title = note,
                    subtitle = "Заметка · Заметки",
                    route = AppRoutes.service(NOTES_ID),
                    relevance = 0.9f,
                )
            }
}
