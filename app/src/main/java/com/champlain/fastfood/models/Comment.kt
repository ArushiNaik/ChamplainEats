package com.champlain.fastfood.models

data class Comment(
    val id: Int = 0,
    val restaurantId: Int = 0,
    val userName: String = "",
    val rating: Int = 5,
    val commentText: String = "",
    val createdAt: String = ""
)
