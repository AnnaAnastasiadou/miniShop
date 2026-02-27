package com.example.minishop.feature.products.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.minishop.R
import com.example.minishop.feature.Product
import com.example.minishop.feature.products.QuantitySelector
import com.example.minishop.feature.toCartProduct
import kotlin.text.replaceFirstChar

@Composable
fun ProductDetailsScreen(
    viewModel: ProductDetailsViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onFavorite = { viewModel.onEvent(DetailsScreenUiEvent.OnToggleFavorite) }
    val onAddItem: () -> Unit = { viewModel.onEvent(DetailsScreenUiEvent.OnAddToCart) }
    val onRemoveItem: (Int) -> Unit =
        { viewModel.onEvent(DetailsScreenUiEvent.OnRemoveFromCart(it)) }
    val onIncreaseItem: (Int, Int) -> Unit =
        { productId, quantity ->
            viewModel.onEvent(
                DetailsScreenUiEvent.OnIncreaseQuantity(
                    productId,
                    quantity
                )
            )
        }
    val onDecreaseItem: (Int, Int) -> Unit =
        { productId, quantity ->
            viewModel.onEvent(
                DetailsScreenUiEvent.OnDecreaseQuantity(
                    productId,
                    quantity
                )
            )
        }
    ProductDetailsContent(
        uiState,
        onBack,
        onFavorite,
        onAddItem = onAddItem,
        onRemoveItem = onRemoveItem,
        onIncreaseItem,
        onDecreaseItem
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsContent(
    uiState: ProductDetailsUiState,
    onBack: () -> Unit,
    onFavorite: () -> Unit,
    onAddItem: () -> Unit,
    onRemoveItem: (Int) -> Unit,
    onIncreaseItem: (Int, Int) -> Unit,
    onDecreaseItem: (Int, Int) -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") }, navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }) { contentPadding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "An Error Occurred",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            uiState.data != null -> {
                ProductDetailsSuccess(
                    product = uiState.data,
                    onFavorite = onFavorite,
                    onAddItem = onAddItem,
                    onRemoveItem = onRemoveItem,
                    onIncreaseItem = onIncreaseItem,
                    onDecreaseItem = onDecreaseItem,
                    modifier = Modifier.padding(contentPadding)
                )
            }


        }
    }
}


@Composable
fun ProductDetailsSuccess(
    product: Product,
    onFavorite: () -> Unit,
    onAddItem: () -> Unit,
    onRemoveItem: (Int) -> Unit,
    onIncreaseItem: (Int, Int) -> Unit,
    onDecreaseItem: (Int, Int) -> Unit,
    modifier: Modifier
) {

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Box {
            AsyncImage(
                model = product.imagePath,
                contentDescription = null,
                placeholder = painterResource(R.drawable.ic_broken_image),
                fallback = painterResource(R.drawable.ic_broken_image),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
                    .background(Color.White)
            )
            IconButton(
                onClick = { onFavorite() },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-16).dp, y = 24.dp)
                    .size(48.dp)
                    .shadow(elevation = 16.dp, shape = CircleShape)
                    .background(
                        color = if (product.isFavorite) Color.Red else Color.White,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_heart),
                    contentDescription = if (product.isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (product.isFavorite) Color.White else Color.Red
                )
            }
        }
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = product.title, fontWeight = FontWeight.Bold, fontSize = 20.sp
                )
                Text(
                    text = "€ ${product.price}", fontWeight = FontWeight.Bold, fontSize = 20.sp
                )
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Text("Category: ${product.category.replaceFirstChar { it.uppercase() }}")
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Text(text = product.description)
            if (product.inCart == 0) {
                Button(
                    onClick = { onAddItem() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.fillMaxWidth(), shape = RectangleShape
                ) {
                    Text(
                        text = "Add to Cart",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            } else {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    QuantitySelector(product.toCartProduct(), onRemoveItem, onIncreaseItem, onDecreaseItem, Modifier.height(48.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProductDetailsSuccess() {
    ProductDetailsContent(
        uiState = ProductDetailsUiState(data = dummyProduct),
        onBack = {},
        onFavorite = {},
        onRemoveItem = {},
        onIncreaseItem = { productId, quantity -> },
        onDecreaseItem = { productId, quantity -> },
        onAddItem = {}
    )
}

val dummyProduct = Product(
    id = 1,
    title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
    price = "109.95",
    category = "men's clothing",
    description = "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday",
    imagePath = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_t.png",
    isFavorite = true,
    inCart = 6
)
