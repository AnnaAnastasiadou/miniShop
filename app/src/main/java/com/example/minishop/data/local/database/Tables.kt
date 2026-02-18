package com.example.minishop.data.local.database

class Tables {
    object Favorites {
        const val TABLE_NAME = "favorite_products"
        const val COLUMN_PRODUCT_ID = "product_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_PRICE = "price"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_IMAGE_PATH = "image_path"
        const val CREATE_TABLE = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_PRODUCT_ID INTEGER PRIMARY KEY,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_PRICE REAL,
                $COLUMN_CATEGORY TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_IMAGE_PATH TEXT
        """
    }
}
