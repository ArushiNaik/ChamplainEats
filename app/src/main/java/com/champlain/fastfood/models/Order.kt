package com.champlain.fastfood.models

data class Order(
    val id: Int = 0,
    val restaurantId: Int = 0,
    val restaurantName: String = "",
    val orderType: String = TYPE_PICKUP,
    var status: String = STATUS_PENDING,
    val totalPrice: Double = 0.0,
    val createdAt: String = "",
    var notes: String = "",
    val itemsSummary: String = ""
) {
    companion object {
        const val TYPE_PICKUP   = "Pickup"
        const val TYPE_DELIVERY = "Delivery"
        const val STATUS_PENDING   = "Pending"
        const val STATUS_CONFIRMED = "Confirmed"
        const val STATUS_PREPARING = "Preparing"
        const val STATUS_READY     = "Ready"
        const val STATUS_DELIVERED = "Delivered"
        const val STATUS_CANCELLED = "Cancelled"
    }
}
