package com.example.minishop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.minishop.data.repository.authorization.AuthRepository
import com.example.minishop.feature.ShopRootNavHost
import com.example.minishop.ui.theme.MiniShopTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val loggedIn = authRepository.isLoggedIn()
        setContent {
            MiniShopTheme {
                ShopRootNavHost(loggedIn)
            }
        }
    }
}

