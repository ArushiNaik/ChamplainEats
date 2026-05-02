package com.champlain.fastfood.models

data class OrderItem(
    val id: Int = 0,
    val orderId: Int = 0,
    val itemName: String = "",
    val quantity: Int = 1,
    val price: Double = 0.0
)
