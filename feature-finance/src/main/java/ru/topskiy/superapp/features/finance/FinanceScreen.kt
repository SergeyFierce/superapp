package ru.topskiy.superapp.features.finance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.topskiy.superapp.core.ui.tokens.Spacing
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun FinanceScreen(
    viewModel: FinanceViewModel = hiltViewModel(),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.md),
    ) {
        Text(
            text = "Финансы",
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "Демонстрация межсервисного взаимодействия через CommandBus и ServiceNavigator",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(Spacing.sm))

        SectionLabel(
            title = "1. CommandBus",
            subtitle = "CreateTaskCommand — обработчик в Планировщике, без зависимости feature→feature",
        )
        Button(
            onClick = { viewModel.dispatchCreateTask("Оплатить интернет") },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("dispatch(CreateTaskCommand)")
        }

        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(4.dp))

        SectionLabel(
            title = "2. ServiceNavigator",
            subtitle = "Только ServiceId(\"planner\") — нет зависимости на другой feature",
        )
        Button(
            onClick = viewModel::openPlanner,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("ServiceNavigator.execute(OpenService)")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = viewModel::goBack,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Назад")
        }
    }
}

@Composable
private fun SectionLabel(title: String, subtitle: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}
