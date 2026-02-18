package com.example.minishop.feature.products.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(
    onProductClick: ()-> Unit
) {
    Column {
        Text("Home")
        Button(onClick = onProductClick) { Text("Go to Details") }
    }
}