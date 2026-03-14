package ru.topskiy.superapp.services.notes

import ru.topskiy.superapp.platform.api.SearchProvider
import ru.topskiy.superapp.platform.api.ServiceId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesSearchProvider @Inject constructor() : SearchProvider {

    override val serviceId: ServiceId = NOTES_ID

    override suspend fun search(query: String): List<SearchResult> = emptyList()
}
