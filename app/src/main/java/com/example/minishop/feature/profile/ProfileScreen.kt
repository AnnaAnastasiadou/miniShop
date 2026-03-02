package com.example.minishop.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minishop.R

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
    val onLogOut: () -> Unit = { viewModel.onEvent(ProfileScreenUiEvent.LogOut)}
    ProfileScreenContent(onLogOut)
}

@Composable
fun ProfileScreenContent(
    onLogOut:() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ){
        Icon(painterResource(R.drawable.ic_avatar), null, modifier = Modifier.size(100.dp))
        Button(onClick = onLogOut, shape = RoundedCornerShape(4.dp)) {
            Text("Log Out")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    ProfileScreenContent(onLogOut = {})
}