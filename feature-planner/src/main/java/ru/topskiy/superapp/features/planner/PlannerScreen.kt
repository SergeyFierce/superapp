package ru.topskiy.superapp.features.planner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ru.topskiy.superapp.core.ui.components.AppCard
import ru.topskiy.superapp.core.ui.components.AppTextField
import ru.topskiy.superapp.core.ui.components.AppTopBar
import ru.topskiy.superapp.core.ui.tokens.Spacing

@Composable
fun PlannerScreen(viewModel: PlannerViewModel = hiltViewModel()) {
    var taskInput by remember { mutableStateOf("") }
    val tasks = remember { mutableStateOf(listOf("Встреча с командой", "Подготовить отчёт", "Позвонить в банк")) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
            modifier = Modifier.fillMaxSize(),
        ) {
            item { AppTopBar(title = "Planner") }
            item {
                AppTextField(
                    value = taskInput,
                    onValueChange = { taskInput = it },
                    label = "Новая задача",
                    placeholder = "Что нужно сделать?",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            items(tasks.value) { task ->
                AppCard(modifier = Modifier.fillMaxWidth()) { Text(task, style = MaterialTheme.typography.bodyLarge) }
            }
        }

        FloatingActionButton(
            onClick = {
                if (taskInput.isNotBlank()) {
                    viewModel.createTask(taskInput)
                    tasks.value = listOf(taskInput) + tasks.value
                    taskInput = ""
                }
            },
            modifier = Modifier.padding(Spacing.md).align(androidx.compose.ui.Alignment.BottomEnd),
        ) {
            Icon(Icons.Outlined.Add, contentDescription = "Создать")
        }
    }
}
