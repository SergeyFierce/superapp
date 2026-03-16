package ru.topskiy.superapp.core.ui.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SectionContainer(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit,
) {
    Section(
        title = title,
        subtitle = subtitle,
        modifier = modifier,
        content = content,
    )
}
