package com.jones.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun OnlineStatusMessage(isOnline: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isOnline) Color(0xFF4CAF50) else Color(0xFFF44336)
        )
    ) {
        Text(
            text = if (isOnline) "🟢 Online" else "🔴 Offline",
            modifier = Modifier.padding(12.dp),
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
