package com.example.minishop.feature

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.minishop.R
import com.example.minishop.feature.cart.CartScreen
import com.example.minishop.feature.login.LogInScreen
import com.example.minishop.feature.products.details.ProductDetailsScreen
import com.example.minishop.feature.products.favorites.FavoriteProductsScreen
import com.example.minishop.feature.products.home.HomeScreen
import com.example.minishop.feature.profile.ProfileScreen

sealed class RootRoute(
    val route: String
) {
    data object Main : RootRoute("main")
    data object LogIn : RootRoute("log_in")
    data object Details : RootRoute("details/{productId}") {
        fun create(productId: Int) = "details/$productId"
    }
}

enum class TabRoutes(val route: String, val label: String, val iconRes: Int) {
    HOME("home", "Home", R.drawable.ic_home),
    FAVORITES("favorites", "Favorites", R.drawable.ic_heart),
    CART("cart", "Cart", R.drawable.ic_shopping_cart),
    PROFILE("profile", "Profile", R.drawable.ic_profile_image)
}

@Composable
fun ShopRootNavHost(
    isLoggedIn: Boolean,
    modifier: Modifier = Modifier.statusBarsPadding()
) {
    val rootNavController = rememberNavController()
    val startDestination = if (isLoggedIn) RootRoute.Main.route else RootRoute.LogIn.route
    NavHost(
        navController = rootNavController, startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = RootRoute.LogIn.route) {
            LogInScreen(
                onLogin = {
                    rootNavController.navigate(RootRoute.Main.route) {
                        popUpTo(RootRoute.LogIn.route) {inclusive = true}
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }
        composable(route = RootRoute.Main.route) {
            MainScreen(onProductClick = { rootNavController.navigate(RootRoute.Details.route) })
        }
        composable(route = RootRoute.Details.route) {
            ProductDetailsScreen(onBack = { rootNavController.popBackStack() })
        }
    }
}

@Composable
fun TabsNavHost(
    navController: NavHostController,
    startDestination: TabRoutes,
    onProductClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
        modifier = modifier.fillMaxSize()
    ) {
        composable(TabRoutes.HOME.route) {
            HomeScreen(onProductClick = onProductClick)
        }
        composable(TabRoutes.FAVORITES.route) {
            FavoriteProductsScreen()
        }
        composable(TabRoutes.CART.route) {
            CartScreen()
        }
        composable(TabRoutes.PROFILE.route) {
            ProfileScreen()
        }
    }
}

@Composable
fun MainScreen(
    onProductClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val startDestination = TabRoutes.HOME

    // Observe nav state so the selected tab updates automatically,
    // even when the back button is clicked
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = modifier, bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                TabRoutes.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true,
                        onClick = {
                            navController.navigate(route = destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(destination.iconRes),
                                contentDescription = null
                            )
                        })
                }
            }
        }
    ) { contentPadding ->
        TabsNavHost(
            navController,
            startDestination,
            onProductClick,
            modifier.padding(contentPadding)
        )
    }
}