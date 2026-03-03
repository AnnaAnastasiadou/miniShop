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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.example.minishop.R
import com.example.minishop.ShopRootViewModel
import com.example.minishop.feature.cart.CartScreen
import com.example.minishop.feature.login.LogInScreen
import com.example.minishop.feature.products.details.ProductDetailsScreen
import com.example.minishop.feature.products.favorites.FavoriteProductsScreen
import com.example.minishop.feature.products.home.HomeScreen
import com.example.minishop.feature.profile.ProfileScreen
import kotlinx.serialization.Serializable

sealed interface RootDestination {
    @Serializable
    data object Main : RootDestination

    @Serializable
    data object LogIn : RootDestination

    @Serializable
    data class Details(val productId: Int) : RootDestination
}

sealed interface TabsDestination {
    @Serializable
    data object Home : TabsDestination

    @Serializable
    data object Favorites : TabsDestination

    @Serializable
    data object Cart : TabsDestination

    @Serializable
    data object Profile : TabsDestination
}

data class TabSpec(
    val label: String,
    val iconRes: Int,
    val destination: TabsDestination
)


val tabs = listOf(
    TabSpec("Home", R.drawable.ic_home, TabsDestination.Home),
    TabSpec("Favorites", R.drawable.ic_heart, TabsDestination.Favorites),
    TabSpec("Cart", R.drawable.ic_shopping_cart, TabsDestination.Cart),
    TabSpec("Profile", R.drawable.ic_avatar, TabsDestination.Profile)
)

@Composable
fun ShopRootNavHost(
    viewModel: ShopRootViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val rootNavController = rememberNavController()
    val startDestination =
        if (uiState.isLoggedIn) RootDestination.Main else RootDestination.LogIn
    NavHost(
        navController = rootNavController,
        startDestination = startDestination,
        modifier = modifier.statusBarsPadding()
    ) {
        composable<RootDestination.LogIn> {
            LogInScreen(
                onLogin = {
                    rootNavController.navigate(RootDestination.Main) {
                        popUpTo(RootDestination.LogIn) { inclusive = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }
        composable<RootDestination.Main> {
            MainScreen(onProductClick = { productId ->
                rootNavController.navigate(RootDestination.Details(productId))
            })
        }
        composable<RootDestination.Details> { backStackEntry ->
            val args = backStackEntry.toRoute<RootDestination.Details>()
            ProductDetailsScreen(onBack = { rootNavController.popBackStack() })
        }
    }
}

@Composable
fun TabsNavHost(
    navController: NavHostController,
    startDestination: TabsDestination,
    onProductClick: (Int) -> Unit,
    onBackToProducts: () -> Unit,
    onContinueShopping: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier.fillMaxSize()
    ) {
        composable<TabsDestination.Home> {
            HomeScreen(onProductClick = onProductClick)
        }
        composable<TabsDestination.Favorites> {
            FavoriteProductsScreen(
                onBackToProducts = onBackToProducts,
                onProductClick = onProductClick
            )
        }
        composable<TabsDestination.Cart> {
            CartScreen(onContinueShopping = onContinueShopping, onProductClick = onProductClick)
        }
        composable<TabsDestination.Profile> {
            ProfileScreen()
        }
    }
}

@Composable
fun MainScreen(
    onProductClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val startDestination = TabsDestination.Home

    // Observe nav state so the selected tab updates automatically,
    // even when the back button is clicked
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = modifier, bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        // hasRoute(RouteClass) returns true if this NavDestination was created from the given @Serializable route type.
                        selected = currentDestination?.hierarchy?.any { it.hasRoute(tab.destination::class)} == true,
                        onClick = {
                            navController.navigate(route = tab.destination) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(tab.iconRes),
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
            onBackToProducts = { navigateToHome(navController) },
            onContinueShopping = { navigateToHome(navController) },
            modifier = Modifier.padding(contentPadding)
        )
    }
}

fun navigateToHome(
    navController: NavHostController
) {
    navController.navigate(route = TabsDestination.Home) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState
        }
        launchSingleTop = true
        restoreState = true
    }
}