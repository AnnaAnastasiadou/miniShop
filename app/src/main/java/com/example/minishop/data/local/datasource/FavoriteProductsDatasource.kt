package com.example.minishop.data.local.datasource

import android.content.ContentValues
import com.example.minishop.data.local.FavoriteProduct
import com.example.minishop.data.local.database.MySqliteOpenHelper
import com.example.minishop.data.local.database.Tables
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoriteProductsDatasource(val dbHelper: MySqliteOpenHelper) {
    private val _favorites = MutableStateFlow<List<FavoriteProduct>>(emptyList())
    val favorites = _favorites.asStateFlow()

    fun addFavorite(product: FavoriteProduct) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(Tables.Favorites.COLUMN_PRODUCT_ID, product.id)
            put(Tables.Favorites.COLUMN_TITLE, product.title)
            put(Tables.Favorites.COLUMN_PRICE, product.price)
            put(Tables.Favorites.COLUMN_DESCRIPTION, product.description)
            put(Tables.Favorites.COLUMN_CATEGORY, product.category)
            put(Tables.Favorites.COLUMN_IMAGE_PATH, product.imagePath)
        }

        db.insert(Tables.Favorites.TABLE_NAME, null, values)
        _favorites.value += product
    }

    fun removeFavorite(productId: Int) {
        val db = dbHelper.writableDatabase
        db.delete(
            Tables.Favorites.TABLE_NAME,
            "${Tables.Favorites.COLUMN_PRODUCT_ID} = ?",
            arrayOf(productId.toString())
        )

        _favorites.value.firstOrNull { it.id == productId }?.let { _favorites.value -= it }
    }

    fun getAllFavorites(): List<FavoriteProduct> {
        val db = dbHelper.readableDatabase
        val favProducts = mutableListOf<FavoriteProduct>()

        val projection = arrayOf(
            Tables.Favorites.COLUMN_PRODUCT_ID,
            Tables.Favorites.COLUMN_TITLE,
            Tables.Favorites.COLUMN_PRICE,
            Tables.Favorites.COLUMN_DESCRIPTION,
            Tables.Favorites.COLUMN_CATEGORY,
            Tables.Favorites.COLUMN_IMAGE_PATH
        )

        val cursor = db.query(
            Tables.Favorites.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        cursor.use {
            while (it.moveToNext()) {
                val favProduct = FavoriteProduct(
                    id = it.getInt(it.getColumnIndexOrThrow(Tables.Favorites.COLUMN_PRODUCT_ID)),
                    title = it.getString(it.getColumnIndexOrThrow(Tables.Favorites.COLUMN_TITLE)),
                    price = it.getDouble(it.getColumnIndexOrThrow(Tables.Favorites.COLUMN_PRICE)),
                    category = it.getString(it.getColumnIndexOrThrow(Tables.Favorites.COLUMN_CATEGORY)),
                    description = it.getString(it.getColumnIndexOrThrow(Tables.Favorites.COLUMN_DESCRIPTION)),
                    imagePath = it.getString(it.getColumnIndexOrThrow(Tables.Favorites.COLUMN_IMAGE_PATH))
                )

                favProducts.add(favProduct)
            }
        }

        _favorites.value = favProducts
        return favProducts

    }

    fun getFavoriteById(productId: Int) {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            Tables.Favorites.COLUMN_PRODUCT_ID,
            Tables.Favorites.COLUMN_TITLE,
            Tables.Favorites.COLUMN_PRICE,
            Tables.Favorites.COLUMN_DESCRIPTION,
            Tables.Favorites.COLUMN_CATEGORY,
            Tables.Favorites.COLUMN_IMAGE_PATH
        )

        val cursor = db.query(
            Tables.Favorites.TABLE_NAME,
            projection,
            "${Tables.Favorites.COLUMN_PRODUCT_ID} = ?",
            arrayOf(productId.toString()),
            null,
            null,
            null
        )
    }

}