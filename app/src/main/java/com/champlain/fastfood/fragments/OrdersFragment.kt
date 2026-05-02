package com.champlain.fastfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.champlain.fastfood.activities.OrderDetailActivity
import com.champlain.fastfood.activities.PlaceOrderActivity
import com.champlain.fastfood.database.DatabaseHelper
import com.champlain.fastfood.models.Order
import com.champlain.fastfood.ui.screens.MyOrdersScreen
import com.champlain.fastfood.ui.theme.ChamplainEatsTheme

class OrdersFragment : Fragment() {
    private lateinit var db: DatabaseHelper
    private val ordersState = mutableStateListOf<Order>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        db = DatabaseHelper.getInstance(requireContext())
        
        return ComposeView(requireContext()).apply {
            setContent {
                ChamplainEatsTheme {
                    MyOrdersScreen(
                        orders = ordersState,
                        onOrderClick = { order ->
                            startActivity(Intent(requireContext(), OrderDetailActivity::class.java)
                                .putExtra("order_id", order.id))
                        },
                        onNewOrderClick = { showPickRestaurantDialog() }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshOrders()
    }

    private fun showPickRestaurantDialog() {
        val restaurants = db.getAllRestaurants()
        val names = restaurants.map { "${it.emoji} ${it.name}" }.toTypedArray()
        AlertDialog.Builder(requireContext())
            .setTitle("Select a Restaurant")
            .setItems(names) { _, which ->
                startActivity(Intent(requireContext(), PlaceOrderActivity::class.java)
                    .putExtra("restaurant_id", restaurants[which].id))
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun refreshOrders() {
        ordersState.clear()
        ordersState.addAll(db.getAllOrders())
    }

    override fun onResume() {
        super.onResume()
        refreshOrders()
    }
}
