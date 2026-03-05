package com.example.minishop.data

import com.example.minishop.data.local.model.CartProductLocal
import com.example.minishop.data.local.model.FavoriteProduct
import com.example.minishop.data.remote.products.ProductDto
import com.example.minishop.feature.Product

val categoriesDto = listOf(
    "electronics", "jewelery", "men's clothing", "women's clothing"
)

val categories = listOf(
    "all", "electronics", "jewelery", "men's clothing", "women's clothing"
)

val productsDto = listOf(
    ProductDto(
        id = 1,
        title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
        price = 109.95,
        category = "men's clothing",
        description = "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday",
        image = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_t.png",
    ), ProductDto(
        id = 2,
        title = "Mens Casual Premium Slim Fit T-Shirts",
        price = 22.20,
        category = "men's clothing",
        description = "Slim-fitting style, contrast raglan long sleeve, three-button henley placket, light weight & soft fabric for breathable and comfortable wearing. And Solid stitched shirts with round neck made for durability and a great fit for casual fashion wear and diehard baseball fans. The Henley style round neckline includes a three-button placket.",
        image = "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_t.png",
    ), ProductDto(
        id = 9,
        title = "WD 2TB Elements Portable External Hard Drive - USB 3.0",
        price = 64.00,
        category = "electronics",
        description = "USB 3.0 and USB 2.0 Compatibility Fast data transfers Improve PC Performance High Capacity; Compatibility Formatted NTFS for Windows 10, Windows 8.1, Windows 7; Reformatting may be required for other operating systems; Compatibility may vary depending on user’s hardware configuration and operating system.",
        image = "https://fakestoreapi.com/img/61IBBVJvSDL._AC_SY879_t.png"
    )
)

val products = listOf(
    Product(
        id = 1,
        title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
        price = "109.95",
        category = "men's clothing",
        description = "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday",
        imagePath = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_t.png",
        isFavorite = false,
        inCart = 0
    ), Product(
        id = 2,
        title = "Mens Casual Premium Slim Fit T-Shirts",
        price = "22.20",
        category = "men's clothing",
        description = "Slim-fitting style, contrast raglan long sleeve, three-button henley placket, light weight & soft fabric for breathable and comfortable wearing. And Solid stitched shirts with round neck made for durability and a great fit for casual fashion wear and diehard baseball fans. The Henley style round neckline includes a three-button placket.",
        imagePath = "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_t.png",
        isFavorite = false,
        inCart = 0
    ), Product(
        id = 9,
        title = "WD 2TB Elements Portable External Hard Drive - USB 3.0",
        price = "64.00",
        category = "electronics",
        description = "USB 3.0 and USB 2.0 Compatibility Fast data transfers Improve PC Performance High Capacity; Compatibility Formatted NTFS for Windows 10, Windows 8.1, Windows 7; Reformatting may be required for other operating systems; Compatibility may vary depending on user’s hardware configuration and operating system.",
        imagePath = "https://fakestoreapi.com/img/61IBBVJvSDL._AC_SY879_t.png",
        isFavorite = false,
        inCart = 0
    )
)

val electronicProducts = listOf(
    Product(
        id = 9,
        title = "WD 2TB Elements Portable External Hard Drive - USB 3.0",
        price = "64.00",
        category = "electronics",
        description = "USB 3.0 and USB 2.0 Compatibility Fast data transfers Improve PC Performance High Capacity; Compatibility Formatted NTFS for Windows 10, Windows 8.1, Windows 7; Reformatting may be required for other operating systems; Compatibility may vary depending on user’s hardware configuration and operating system.",
        imagePath = "https://fakestoreapi.com/img/61IBBVJvSDL._AC_SY879_t.png",
        isFavorite = false,
        inCart = 0
    )
)

val productDetailsDto = ProductDto(
    id = 1,
    title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
    price = 109.95,
    category = "men's clothing",
    description = "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday",
    image = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_t.png",
)

val productDetails = Product(
    id = 1,
    title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
    price = "109.95",
    category = "men's clothing",
    description = "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday",
    imagePath = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_t.png",
    isFavorite = false,
    inCart = 0
)

val favoriteProduct = FavoriteProduct(
    id = 1,
    title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
    price = 109.95,
    category = "men's clothing",
    description = "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday",
    imagePath = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_t.png",
)

val cartProductLocale = CartProductLocal(
    id = 1,
    title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
    price = 109.95,
    quantity = 1,
    imagePath = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_t.png",
)