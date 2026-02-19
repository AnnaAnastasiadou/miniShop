package com.example.minishop.feature.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.minishop.R

@Composable
fun LogInScreen(
    viewModel: LogInViewModel = hiltViewModel(), onLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onClick: (String, String) -> Unit =
        { username, password -> viewModel.logIn(username, password) }
    val onErrorShown: () -> Unit = { viewModel.clearError() }
    LogInContent(uiState, onClick, onLogin, onErrorShown)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInContent(
    uiState: LogInUiState,
    onClick: (String, String) -> Unit,
    onLogin: () -> Unit,
    onErrorShown: () -> Unit
) {
    var usernameText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "MiniShop", fontWeight = FontWeight.Bold, fontSize = 40.sp)
                    Icon(
                        painter = painterResource(R.drawable.ic_shopping_cart),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }, expandedHeight = 200.dp, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }, snackbarHost = {
        SnackbarHost(hostState = snackBarHostState) { data ->
            Snackbar(
                snackbarData = data,
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        }
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Username", fontSize = 20.sp)
                OutlinedTextField(
                    value = usernameText,
                    onValueChange = { usernameText = it },
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Password", fontSize = 20.sp)
                OutlinedTextField(
                    value = passwordText,
                    onValueChange = { passwordText = it },
                )
            }

            Button(
                onClick = { onClick(usernameText, passwordText) },
                shape = RectangleShape,
                enabled = !uiState.isLoading
            ) {
                Text(
                    text = "LOGIN",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(4.dp)
                )
            }


        }
    }

    LaunchedEffect(uiState.error) {
        if (!uiState.error.isNullOrBlank()) {
            snackBarHostState.showSnackbar("Invalid username or password")
            onErrorShown()
        }
    }

    LaunchedEffect(uiState.success) {
        if (!uiState.success) {
            onLogin()
        }
    }
}

@Composable
@Preview
fun PreviewLoginScreen() {
    LogInContent(
        uiState = LogInUiState(isLoading = true),
        onClick = { username, password -> },
        onLogin = {},
        onErrorShown = {})
}