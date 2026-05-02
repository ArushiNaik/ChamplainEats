package com.champlain.fastfood.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.champlain.fastfood.database.DatabaseHelper
import com.champlain.fastfood.models.Order
import com.champlain.fastfood.ui.screens.PlaceOrderScreen
import com.champlain.fastfood.ui.theme.ChamplainEatsTheme
import com.google.android.material.snackbar.Snackbar

class PlaceOrderActivity : ComponentActivity() {

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val restaurantId = intent.getIntExtra("restaurant_id", -1)
        if (restaurantId == -1) { finish(); return }

        db = DatabaseHelper.getInstance(this)
        val restaurant = db.getRestaurantById(restaurantId) ?: run { finish(); return }
        val foodItems = db.getFoodItemsByRestaurant(restaurantId)

        setContent {
            ChamplainEatsTheme {
                PlaceOrderScreen(
                    restaurantName = restaurant.name,
                    foodItems = foodItems,
                    discountPercent = restaurant.discountPercent,
                    onBackClick = { onBackPressedDispatcher.onBackPressed() },
                    onPlaceOrderClick = { orderType, notes ->
                        val selected = foodItems.filter { it.quantity > 0 }
                        if (selected.isEmpty()) {
                            Snackbar.make(window.decorView, "Please add at least one item.", Snackbar.LENGTH_SHORT).show()
                        } else {
                            val summary = selected.joinToString(", ") { "${it.quantity}x ${it.name}" }
                            val subtotal = selected.sumOf { it.price * it.quantity }
                            val total = subtotal - subtotal * restaurant.discountPercent / 100.0

                            val orderId = db.insertOrder(
                                Order(
                                    restaurantId = restaurantId,
                                    restaurantName = restaurant.name,
                                    orderType = orderType,
                                    status = Order.STATUS_PENDING,
                                    totalPrice = total,
                                    notes = notes,
                                    itemsSummary = summary
                                )
                            )
                            db.insertOrderItems(orderId, selected)
                            Snackbar.make(window.decorView, "Order placed successfully! 🎉", Snackbar.LENGTH_LONG).show()
                            finish()
                        }
                    }
                )
            }
        }
    }
}
