package com.champlain.fastfood.models

data class FoodItem(
    val id: Int = 0,
    val restaurantId: Int = 0,
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val category: String = "",
    var quantity: Int = 0
)
