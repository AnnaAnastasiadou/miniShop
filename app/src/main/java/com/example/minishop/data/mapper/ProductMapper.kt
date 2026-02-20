package com.example.minishop.data.mapper

import com.example.minishop.data.remote.products.ProductDto
import com.example.minishop.feature.Product
import com.example.minishop.feature.products.home.Category

fun ProductDto.toProduct(isFav: Boolean = false): Product {
    return Product(
        id = id,
        title = title,
        price = price,
        category = category,
        description = description,
        imagePath = image,
        isFavorite = isFav
    )
}