package com.champlain.fastfood.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.compose.runtime.*
import com.champlain.fastfood.database.DatabaseHelper
import com.champlain.fastfood.models.Order
import com.champlain.fastfood.models.OrderItem
import com.champlain.fastfood.ui.screens.OrderDetailScreen
import com.champlain.fastfood.ui.theme.ChamplainEatsTheme
import com.google.android.material.snackbar.Snackbar
import android.widget.EditText

class OrderDetailActivity : ComponentActivity() {

    private lateinit var db: DatabaseHelper
    private var orderState by mutableStateOf<Order?>(null)
    private var itemsState by mutableStateOf<List<OrderItem>>(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val orderId = intent.getIntExtra("order_id", -1)
        if (orderId == -1) { finish(); return }

        db = DatabaseHelper.getInstance(this)
        refreshData(orderId)

        setContent {
            ChamplainEatsTheme {
                orderState?.let { order ->
                    OrderDetailScreen(
                        order = order,
                        items = itemsState,
                        onBackClick = { onBackPressedDispatcher.onBackPressed() },
                        onUpdateStatusClick = { showStatusDialog(order) },
                        onEditNotesClick = { showEditNotesDialog(order) },
                        onCancelOrderClick = { showCancelDialog(order) }
                    )
                }
            }
        }
    }

    private fun refreshData(orderId: Int) {
        orderState = db.getOrderById(orderId)
        itemsState = db.getOrderItems(orderId)
    }

    private fun showStatusDialog(order: Order) {
        val statuses = arrayOf(Order.STATUS_CONFIRMED, Order.STATUS_PREPARING,
                               Order.STATUS_READY, Order.STATUS_DELIVERED)
        AlertDialog.Builder(this).setTitle("Update Status")
            .setItems(statuses) { _, i ->
                db.updateOrderStatus(order.id, statuses[i])
                refreshData(order.id)
                Snackbar.make(window.decorView, "Status → ${statuses[i]}", Snackbar.LENGTH_SHORT).show()
            }.show()
    }

    private fun showEditNotesDialog(order: Order) {
        val et = EditText(this).apply { 
            setText(order.notes)
            setPadding(48, 32, 48, 32) 
        }
        AlertDialog.Builder(this).setTitle("Edit Instructions")
            .setView(et)
            .setPositiveButton("Save") { _, _ ->
                val newNotes = et.text.toString().trim()
                db.updateOrderNotes(order.id, newNotes)
                refreshData(order.id)
                Snackbar.make(window.decorView, "Instructions updated.", Snackbar.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null).show()
    }

    private fun showCancelDialog(order: Order) {
        AlertDialog.Builder(this)
            .setTitle("Cancel Order")
            .setMessage("Are you sure you want to cancel this order?")
            .setPositiveButton("Yes, Cancel") { _, _ ->
                db.updateOrderStatus(order.id, Order.STATUS_CANCELLED)
                refreshData(order.id)
                Snackbar.make(window.decorView, "Order cancelled.", Snackbar.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null).show()
    }
}
