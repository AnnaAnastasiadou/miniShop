package com.example.minishop.feature.products

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.minishop.R
import com.example.minishop.data.local.model.CartProductLocal
import com.example.minishop.data.repository.cart.CartProduct
@Composable
fun QuantitySelector(
    product: CartProduct,
    onRemoveItem: (Int) -> Unit,
    onIncreaseItem: (Int, Int) -> Unit,
    onDecreaseItem: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 45.dp,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .height(height)
                .border(1.dp, MaterialTheme.colorScheme.outline, RectangleShape),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onDecreaseItem(product.id, product.quantity) },
                modifier = Modifier.fillMaxHeight()
            ) { Icon(painterResource(R.drawable.ic_minus), contentDescription = stringResource(R.string.reduce_item)) }

            VerticalDivider(Modifier.fillMaxHeight(), color = MaterialTheme.colorScheme.outline)

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(min = 30.dp)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text(product.quantity.toString(), style = MaterialTheme.typography.titleMedium)
            }

            VerticalDivider(Modifier.fillMaxHeight(), color = MaterialTheme.colorScheme.outline)

            IconButton(
                onClick = { onIncreaseItem(product.id, product.quantity) },
                modifier = Modifier.fillMaxHeight()
            ) { Icon(painterResource(R.drawable.ic_plus), contentDescription = stringResource(R.string.increase_item)) }
        }

        IconButton(
            onClick = { onRemoveItem(product.id) },
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .height(height)
                .wrapContentWidth(),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Icon(
                painterResource(R.drawable.ic_remove),
                stringResource(R.string.remove),
            )
        }
    }
}

val dummyProduct = CartProduct(
    id = 1,
    title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
    price = 109.95,
    imagePath = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_t.png",
    quantity = 2,
    totalPrice = 2 * 109.95,
)

@Preview
@Composable
fun PreviewQuantitySelector() {
    QuantitySelector(
        product = dummyProduct,
        onRemoveItem = { i: Int -> },
        onIncreaseItem = { i1: Int, i2: Int -> },
        onDecreaseItem = { i1: Int, i2: Int -> },
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth()
    )
}