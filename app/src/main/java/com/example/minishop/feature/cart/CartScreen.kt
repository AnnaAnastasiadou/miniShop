package com.example.minishop.feature.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.minishop.R
import com.example.minishop.data.local.model.CartProductLocal
import com.example.minishop.data.repository.cart.CartProduct
import com.example.minishop.feature.priceFormatter
import com.example.minishop.feature.products.QuantitySelector

@Composable
fun CartScreen(
    viewModel: CartViewModel = hiltViewModel(),
    onContinueShopping: () -> Unit,
    onProductClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onRemoveItem: (Int) -> Unit = { productId ->
        viewModel.onEvent(
            CartScreenUiEvent.OnRemoveFromCart(
                productId
            )
        )
    }
    val onIncreaseItem: (Int, Int) -> Unit = { productId, quantity ->
        viewModel.onEvent(
            CartScreenUiEvent.OnIncreaseQuantity(
                productId, quantity
            )
        )
    }
    val onDecreaseItem: (Int, Int) -> Unit = { productId, quantity ->
        viewModel.onEvent(
            CartScreenUiEvent.OnDecreaseQuantity(
                productId, quantity
            )
        )
    }
    val onCheckout: () -> Unit = { viewModel.onEvent(CartScreenUiEvent.OnCheckout) }

    val onBackToCart: () -> Unit = {viewModel.onEvent(CartScreenUiEvent.OnCloseCheckoutDialog)}
    CartScreenContent(uiState, onProductClick, onRemoveItem, onIncreaseItem, onDecreaseItem, onCheckout, onContinueShopping, onBackToCart)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreenContent(
    uiState: CartProductsUiState,
    onProductClick: (Int) -> Unit,
    onRemoveItem: (Int) -> Unit,
    onIncreaseItem: (Int, Int) -> Unit,
    onDecreaseItem: (Int, Int) -> Unit,
    onCheckout: () -> Unit,
    onContinueShopping: () -> Unit,
    onBackToCart: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(painter = painterResource(R.drawable.ic_shopping_cart), null)
                        Text("Cart", fontWeight = FontWeight.Bold)
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(start = 8.dp, top = 8.dp, end = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (uiState.data.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.data) { product ->
                        CartProductCard(
                            product, onRemoveItem, onIncreaseItem, onDecreaseItem, onProductClick
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Nothing in your cart yet", fontSize = 16.sp)
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total")
                    Text("€${priceFormatter(uiState.totalPrice)}", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onCheckout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(4.dp),
                    enabled = !(uiState.data.isEmpty())
                ) {
                    Text("Checkout")
                }
            }
        }
    }
    CheckoutDialog(
        uiState.checkoutUiState,
        onContinueShopping = onContinueShopping,
        onBackToCart = onBackToCart
    )
}

@Composable
fun CheckoutDialog(
    checkoutUiState: CheckoutUiState,
    onContinueShopping: () -> Unit,
    onBackToCart: () -> Unit

) {
    when {
        checkoutUiState.isLoading -> {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("Checking out...") },
                text = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                },
                confirmButton = {}
            )
        }

        checkoutUiState.success != null -> {
            val checkoutData = checkoutUiState.success
            AlertDialog(
                onDismissRequest = onBackToCart,
                title = { Text("Order placed successfully!") },
                text = {
                    Column {
                        Text("Order ID: ${checkoutData.id}")
                        Text("Date: ${dateFormatter(checkoutData.date)}")
                        Text("Items: ${checkoutData.products.size}")
                    }
                },
                confirmButton = {
                    Button(onClick = onContinueShopping) {
                        Text("Continue Shopping")
                    }
                }
            )
        }

        checkoutUiState.error != null -> {
            AlertDialog(
                onDismissRequest = onBackToCart,
                title = { Text("Checkout Failed") },
                text = { Text(checkoutUiState.error) },
                confirmButton = {},
                dismissButton = { Button(onClick = onBackToCart) { Text("Cancel") } }
            )
        }
    }

}

@Composable
fun CartProductCard(
    product: CartProduct,
    onRemoveItem: (Int) -> Unit,
    onIncreaseItem: (Int, Int) -> Unit,
    onDecreaseItem: (Int, Int) -> Unit,
    onProductClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        onClick = { onProductClick(product.id) }
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = product.imagePath,
                fallback = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.ic_broken_image),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.7f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1.5f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        product.title,
                        modifier = Modifier.weight(2f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        "€${priceFormatter(product.totalPrice)}",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End
                    )
                }
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                    ) {
                        QuantitySelector(
                            product = product,
                            onRemoveItem = onRemoveItem,
                            onIncreaseItem = onIncreaseItem,
                            onDecreaseItem = onDecreaseItem,
                            height = 35.dp
                        )
                    }
                }
            }
        }
    }
}

val dummyCartProducts = listOf(
    CartProduct(
        id = 1,
        title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
        price = 109.95,
        imagePath = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_t.png",
        quantity = 2,
        totalPrice = 2 * 109.95,
    ), CartProduct(
        id = 2,
        title = "Mens Casual Premium Slim Fit T-Shirts ",
        price = 22.30,
        imagePath = "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_t.png",
        quantity = 1,
        totalPrice = 22.30
    ), CartProduct(
        id = 1,
        title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
        price = 109.95,
        imagePath = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_t.png",
        quantity = 2,
        totalPrice = 2 * 109.95,
    ), CartProduct(
        id = 2,
        title = "Mens Casual Premium Slim Fit T-Shirts ",
        price = 22.30,
        imagePath = "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_t.png",
        quantity = 1,
        totalPrice = 22.30
    ), CartProduct(
        id = 1,
        title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
        price = 109.95,
        imagePath = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_t.png",
        quantity = 2,
        totalPrice = 2 * 109.95,
    ), CartProduct(
        id = 2,
        title = "Mens Casual Premium Slim Fit T-Shirts ",
        price = 22.30,
        imagePath = "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_t.png",
        quantity = 1,
        totalPrice = 22.30
    )
)

@Preview(showBackground = true)
@Composable
fun PreviewCartScreen() {
    CartScreenContent(
        CartProductsUiState(data = dummyCartProducts),
        onRemoveItem = {},
        onIncreaseItem = { i1, i2 -> },
        onDecreaseItem = { i1, i2 -> },
        onCheckout = {},
        onContinueShopping = {},
        onBackToCart = {},
        onProductClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewEmptyCart() {
    CartScreenContent(
        CartProductsUiState(data = emptyList()),
        onRemoveItem = {},
        onIncreaseItem = { i1, i2 -> },
        onDecreaseItem = { i1, i2 -> },
        onCheckout = {},
        onContinueShopping = {},
        onBackToCart = {},
        onProductClick = {}
    )
}

@Preview
@Composable
fun PreviewCheckoutDialogLoading() {
    CheckoutDialog(
        checkoutUiState = CheckoutUiState(isLoading = true),
        onContinueShopping = {},
        onBackToCart = {},
    )
}

@Preview
@Composable
fun PreviewCheckoutDialogSuccess() {
    CheckoutDialog(
        checkoutUiState = CheckoutUiState(
            success = CartCheckout(
                id = 1,
                date = "2020-03-02T00:00:00.000Z",
                products = listOf(
                    CheckoutProduct(
                        id = 1,
                        quantity = 4
                    )
                )
            )
        ),
        onContinueShopping = {},
        onBackToCart = {}
    )
}

@Preview
@Composable
fun PreviewCheckoutDialogError() {
    CheckoutDialog(
        checkoutUiState = CheckoutUiState(
            error = "An Unexpected Error Occurred"
        ),
        onContinueShopping = {},
        onBackToCart = {},
    )
}