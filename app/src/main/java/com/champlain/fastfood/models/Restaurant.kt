package com.champlain.fastfood.models

data class Restaurant(
    val id: Int = 0,
    val name: String = "",
    val category: String = "",
    val description: String = "",
    val openTime: String = "",
    val closeTime: String = "",
    val rating: Double = 4.0,
    val isCampus: Boolean = false,
    val discountPercent: Int = 0,
    val emoji: String = "🍽️",
    val deliveryMins: Int = 25,
    val bannerDrawable: String = "bg_burger"   // drawable name for banner gradient
)
