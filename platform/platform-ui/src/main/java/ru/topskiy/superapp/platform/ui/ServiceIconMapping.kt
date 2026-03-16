package ru.topskiy.superapp.platform.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Note
import androidx.compose.ui.graphics.vector.ImageVector

fun String.asImageVector(): ImageVector = when (this) {
    "planner" -> Icons.Default.Event
    "notes" -> Icons.Default.Note
    "diary" -> Icons.Default.MenuBook
    "finance" -> Icons.Default.AccountBalanceWallet
    else -> Icons.Default.Note
}
