package ru.topskiy.superapp.features.planner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PlannerScreen(
    viewModel: PlannerViewModel = hiltViewModel(),
) {
    var taskInput by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(
            text = "Планировщик",
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "Создайте задачу — событие улетит в EventBus",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = taskInput,
            onValueChange = { taskInput = it },
            label = { Text("Название задачи") },
            placeholder = { Text("Например: Позвонить партнёру") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Button(
            onClick = {
                if (taskInput.isNotBlank()) {
                    viewModel.createTask(taskInput)
                    taskInput = ""
                }
            },
            enabled = taskInput.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Создать задачу")
        }

        Text(
            text = "После нажатия — откройте Заметки и увидите событие",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.tertiary,
        )
    }
}
