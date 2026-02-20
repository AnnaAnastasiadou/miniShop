package com.example.minishop.feature.products.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.minishop.R
import com.example.minishop.feature.Product
import dagger.hilt.android.lifecycle.HiltViewModel


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(), onProductClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onCategoryClick: (String) -> Unit = { viewModel.loadProductsByCategory(category = it) }
    HomeScreenContent(uiState, onCategoryClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    uiState: HomeUiState, onCategoryClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MiniShop", fontWeight = FontWeight.Bold, fontSize = 24.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }) {
        Column(modifier = Modifier.padding(it)) {
            val isLoading = uiState.categoriesUiState.isLoading || uiState.productsUiState.isLoading
            if (isLoading) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) { LinearProgressIndicator(Modifier.fillMaxWidth()) }

            }
            Categories(uiState.categoriesUiState, onCategoryClick)
            ProductList(uiState.productsUiState)
        }
    }
}

@Composable
fun Categories(
    categoriesUiState: CategoriesUiState, onCategoryClick: (String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("all") }
    when {
        categoriesUiState.error != null -> {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) { Text(categoriesUiState.error, color = MaterialTheme.colorScheme.error) }
        }

        categoriesUiState.data != null -> {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                items(categoriesUiState.data) { category ->
                    val categoryLowerCase = category.lowercase()
                    val isSelected = selectedCategory == categoryLowerCase
                    Text(
                        text = categoryLowerCase.replaceFirstChar { char -> char.uppercase() },
                        color = if (isSelected)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background( if(isSelected)MaterialTheme.colorScheme.primary else Color.Transparent)
                            .clickable {
                                onCategoryClick(categoryLowerCase)
                                selectedCategory = categoryLowerCase
                            }
                            .padding(vertical = 8.dp, horizontal = 16.dp))
                }
            }
        }
    }
}

@Composable
fun ProductList(productsUiState: ProductsUiState) {
    when {
        productsUiState.error != null -> {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) { Text(productsUiState.error, color = MaterialTheme.colorScheme.error) }
        }

        productsUiState.data != null -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                items(productsUiState.data) { product ->
                    ProductCard(product)
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column {
            AsyncImage(
                model = product.imagePath,
                contentDescription = null,
                placeholder = painterResource(R.drawable.ic_broken_image),
                fallback = painterResource(R.drawable.ic_broken_image),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .background(Color.White)
            )
            Column(
                modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = product.title,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(3f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (product.isFavorite) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_heart),
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Text("€${product.price}", fontWeight = FontWeight.Bold)
            }
        }
    }
}


val dummyCategories = listOf(
    "all", "electronics", "jewelery", "men's clothing", "women's clothing"
)

val dummyProducts = listOf(
    Product(
        id = 1,
        title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
        price = 109.95,
        category = "men's clothing",
        description = "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday",
        imagePath = null,
        isFavorite = true,
    ), Product(
        id = 1,
        title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
        price = 109.95,
        category = "",
        description = "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday",
        imagePath = null,
        isFavorite = false,
    ), Product(
        id = 1,
        title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
        price = 109.95,
        category = "",
        description = "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday",
        imagePath = null,
        isFavorite = true,
    ), Product(
        id = 1,
        title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
        price = 109.95,
        category = "",
        description = "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday",
        imagePath = null,
        isFavorite = false,
    ), Product(
        id = 1,
        title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
        price = 109.95,
        category = "",
        description = "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday",
        imagePath = null,
        isFavorite = true,
    ), Product(
        id = 1,
        title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
        price = 109.95,
        category = "",
        description = "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday",
        imagePath = null,
        isFavorite = true,
    )
)

@Preview
@Composable
fun PreviewHomeContent() {
    HomeScreenContent(
        uiState = HomeUiState(
            CategoriesUiState(data = dummyCategories), ProductsUiState(data = dummyProducts)
        ), onCategoryClick = {})
}