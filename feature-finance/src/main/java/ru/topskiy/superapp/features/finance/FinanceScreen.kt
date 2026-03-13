package ru.topskiy.superapp.features.finance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ru.topskiy.superapp.core.ui.components.AppButton
import ru.topskiy.superapp.core.ui.components.AppCard
import ru.topskiy.superapp.core.ui.components.AppTopBar
import ru.topskiy.superapp.core.ui.tokens.Spacing

@Composable
fun FinanceScreen(viewModel: FinanceViewModel = hiltViewModel()) {
    val transactions = listOf("Продукты -1 450 ₽", "Зарплата +120 000 ₽", "Интернет -600 ₽")

    LazyColumn(
        contentPadding = PaddingValues(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        modifier = Modifier.fillMaxSize(),
    ) {
        item { AppTopBar(title = "Finance") }

        items(transactions) { transaction ->
            AppCard(modifier = Modifier.fillMaxWidth()) {
                Text(transaction, style = MaterialTheme.typography.bodyLarge)
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm), modifier = Modifier.fillMaxWidth()) {
                AppButton(text = "Создать задачу", onClick = { viewModel.dispatchCreateTask("Оплатить интернет") }, modifier = Modifier.weight(1f))
                AppButton(text = "Открыть Planner", onClick = viewModel::openPlanner, modifier = Modifier.weight(1f))
            }
        }
    }
}
