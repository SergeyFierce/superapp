package ru.topskiy.superapp.core.services.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Note
import androidx.compose.ui.graphics.vector.ImageVector
import ru.topskiy.superapp.core.services.ServiceIcon

fun ServiceIcon.asImageVector(): ImageVector = when (this) {
    is ServiceIcon.Key -> when (name) {
        "planner" -> Icons.Default.Event
        "notes" -> Icons.Default.Note
        "finance" -> Icons.Default.AccountBalanceWallet
        else -> Icons.Default.Note
    }
}
