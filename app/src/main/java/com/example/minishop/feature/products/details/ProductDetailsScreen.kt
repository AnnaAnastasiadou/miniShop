package com.example.minishop.feature.products.details

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ProductDetailsScreen(
    onBack: () -> Unit
) {
    Column {
        Text("Details")
        Button(onClick = onBack) { Text("Back") }
    }
}