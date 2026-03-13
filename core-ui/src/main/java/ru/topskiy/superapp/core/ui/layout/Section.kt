package ru.topskiy.superapp.core.ui.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.topskiy.superapp.core.ui.components.SectionHeader

/**
 * Секция с заголовком и контентом.
 */
@Composable
fun Section(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        SectionHeader(
            title = title,
            subtitle = subtitle,
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            content = content,
        )
    }
}
