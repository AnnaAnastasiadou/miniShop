package com.example.minishop.feature.products.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.minishop.R
import com.example.minishop.data.local.model.FavoriteProduct
import com.example.minishop.feature.Product

@Composable
fun FavoriteProductsScreen(
    viewModel: FavoriteProductsViewModel = hiltViewModel(),
    onBackToProducts: () -> Unit,
    onProductClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FavoriteProductsContent(uiState, onProductClick, onBackToProducts)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteProductsContent(
    uiState: FavoriteProductsUiState,
    onProductClick: (Int) -> Unit,
    onBackToProducts: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(painterResource(R.drawable.ic_heart), null)
                        Text("Favorites")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { contentPadding ->
        FavoriteProductsList(
            products = uiState.data,
            onBackToProducts = onBackToProducts,
            onProductClick = onProductClick,
            modifier = Modifier.padding(contentPadding)
        )
    }
}

@Composable
fun FavoriteProductsList(
    products: List<FavoriteProduct>,
    onBackToProducts: () -> Unit,
    onProductClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (products.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("No favorites yet", fontSize = 24.sp)
            HorizontalDivider(
                Modifier
                    .width(80.dp)
                    .padding(top = 8.dp),
                thickness = 4.dp,
                color = MaterialTheme.colorScheme.primary
            )
            TextButton(
                onClick = {onBackToProducts()}
            ) {
                Text(
                    text = "Browse products",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 22.sp,
                    textDecoration = TextDecoration.Underline,
                )
            }
        }
    } else {
        LazyColumn(
            modifier = modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(products) { product ->
                FavoriteProductCard(onProductClick, product)
            }
        }
    }
}

@Composable
fun FavoriteProductCard(
    onProductClick: (Int) -> Unit,
    product: FavoriteProduct
) {
    Card(
        onClick = { onProductClick(product.id) },
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {
            AsyncImage(
                model = product.imagePath,
                fallback = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.ic_broken_image),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White),
            )
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = product.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "€${product.price}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

val dummyProducts = listOf(
    FavoriteProduct(
        id = 1,
        title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
        price = 109.95,
        category = "men's clothing",
        description = "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday",
        imagePath = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_t.png",
    ), FavoriteProduct(
        id = 2,
        title = "Mens Casual Premium Slim Fit T-Shirts ",
        price = 22.30,
        category = "men's category",
        description = "Slim-fitting style, contrast raglan long sleeve, three-button henley placket, light weight & soft fabric for breathable and comfortable wearing. And Solid stitched shirts with round neck made for durability and a great fit for casual fashion wear and diehard baseball fans. The Henley style round neckline includes a three-button placket.",
        imagePath = "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_t.png",
    )
)

val dummyEmptyFavorites = emptyList<FavoriteProduct>()


@Preview
@Composable
fun PreviewFavoriteProductsContent() {
    FavoriteProductsContent(
        uiState = FavoriteProductsUiState(data = dummyProducts),
        onBackToProducts = {},
        onProductClick = {}
    )
}

@Preview
@Composable
fun PreviewFavoriteProductsEmpty() {
    FavoriteProductsContent(
        uiState = FavoriteProductsUiState(data = dummyEmptyFavorites),
        onBackToProducts = {},
        onProductClick = {}
    )
}
