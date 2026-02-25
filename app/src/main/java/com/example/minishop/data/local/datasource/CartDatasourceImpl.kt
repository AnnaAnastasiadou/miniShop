package com.example.minishop.data.local.datasource

import android.content.ContentValues
import com.example.minishop.data.local.database.MySqliteOpenHelper
import com.example.minishop.data.local.database.Tables
import com.example.minishop.data.local.model.CartProduct
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class CartDatasourceImpl @Inject constructor(val dbHelper: MySqliteOpenHelper) : CartDatasource {
    private val _cartProducts = MutableStateFlow<List<CartProduct>>(emptyList())
    override val cartProducts = _cartProducts.asStateFlow()

    override fun addItem(cartProduct: CartProduct) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(Tables.Cart.COLUMN_PRODUCT_ID, cartProduct.id)
            put(Tables.Cart.COLUMN_QUANTITY, 1)
            put(Tables.Cart.COLUMN_TITLE, cartProduct.title)
            put(Tables.Cart.COLUMN_PRICE, cartProduct.price)
            put(Tables.Cart.COLUMN_IMAGE_PATH, cartProduct.price)
        }

        db.insert(Tables.Cart.TABLE_NAME, null, values)
        _cartProducts.value += cartProduct
    }

    override fun removeItem(productId: Int) {
        val db = dbHelper.writableDatabase
        db.delete(
            Tables.Cart.TABLE_NAME,
            "${Tables.Cart.COLUMN_PRODUCT_ID} = ?",
            arrayOf(productId.toString())
        )

        _cartProducts.value.firstOrNull { it.id == productId }?.let { _cartProducts.value -= it }
    }

    override fun updateQuantity(productId: Int, newQuantity: Int) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(Tables.Cart.COLUMN_QUANTITY, newQuantity)
        }
        db.update(
            Tables.Cart.TABLE_NAME,
            values,
            "${Tables.Cart.COLUMN_PRODUCT_ID} = ?",
            arrayOf(productId.toString())
        )

        _cartProducts.update { list ->
            list.map { product ->
                if (product.id == productId) {
                    product.copy(quantity = newQuantity)
                } else product
            }
        }
    }

    override fun getQuantityById(productId: Int): Int? {
        val db = dbHelper.readableDatabase

        val columns = arrayOf(Tables.Cart.COLUMN_QUANTITY)
        val cursor = db.query(
            Tables.Cart.TABLE_NAME,
            columns,
            "${Tables.Cart.COLUMN_PRODUCT_ID} = ?",
            arrayOf(productId.toString()),
            null,
            null,
            null
        )
        cursor.use {
            while (it.moveToNext()) {
                return it.getInt(it.getColumnIndexOrThrow(Tables.Cart.COLUMN_QUANTITY))
            }
        }
        return null
    }

    override fun clearCart() {
        val db = dbHelper.writableDatabase
        db.delete(Tables.Cart.TABLE_NAME, null, null)
        _cartProducts.value = emptyList()
    }

    override fun getCartItems(): List<CartProduct> {
        val db = dbHelper.readableDatabase
        val productList = mutableListOf<CartProduct>()

        val columns = arrayOf(
            Tables.Cart.COLUMN_PRODUCT_ID,
            Tables.Cart.COLUMN_QUANTITY,
            Tables.Cart.COLUMN_TITLE,
            Tables.Cart.COLUMN_PRICE,
            Tables.Cart.COLUMN_IMAGE_PATH
        )

        val cursor = db.query(
            Tables.Cart.TABLE_NAME,
            columns,
            null,
            null,
            null,
            null,
            null
        )

        cursor.use {
            while (it.moveToNext()) {
                val cartProduct = CartProduct(
                    id = it.getInt(it.getColumnIndexOrThrow(Tables.Cart.COLUMN_PRODUCT_ID)),
                    quantity = it.getInt(it.getColumnIndexOrThrow(Tables.Cart.COLUMN_QUANTITY)),
                    title = it.getString(it.getColumnIndexOrThrow(Tables.Cart.COLUMN_TITLE)),
                    price = it.getDouble(it.getColumnIndexOrThrow(Tables.Cart.COLUMN_PRICE)),
                    imagePath = it.getString(it.getColumnIndexOrThrow(Tables.Cart.COLUMN_IMAGE_PATH))
                )
                productList.add(cartProduct)
            }
            _cartProducts.value = productList
        }

        return productList
    }
}