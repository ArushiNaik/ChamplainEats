package com.champlain.fastfood.activities

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.champlain.fastfood.R
import com.champlain.fastfood.database.DatabaseHelper
import com.champlain.fastfood.databinding.ActivityOrderDetailBinding
import com.champlain.fastfood.models.Order
import com.google.android.material.snackbar.Snackbar

class OrderDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailBinding
    private lateinit var db: DatabaseHelper
    private lateinit var order: Order

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orderId = intent.getIntExtra("order_id", -1)
        if (orderId == -1) { finish(); return }

        db    = DatabaseHelper.getInstance(this)
        order = db.getOrderById(orderId) ?: run { finish(); return }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply { setDisplayHomeAsUpEnabled(true); title = "Order #${order.id}" }
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        populateDetails()
        populateItems()

        binding.btnUpdateStatus.setOnClickListener { showStatusDialog() }
        binding.btnEditNotes.setOnClickListener   { showEditNotesDialog() }
        binding.btnCancelOrder.setOnClickListener { showCancelDialog() }

        if (order.status == Order.STATUS_CANCELLED || order.status == Order.STATUS_DELIVERED) {
            binding.btnCancelOrder.visibility    = View.GONE
            binding.btnUpdateStatus.visibility   = View.GONE
        }
    }

    private fun populateDetails() {
        binding.tvRestaurant.text = order.restaurantName
        binding.tvType.text       = if (order.orderType == Order.TYPE_DELIVERY) "🛵 Delivery" else "🛍️ Pickup"
        binding.tvDate.text       = order.createdAt
        binding.tvTotal.text      = "$%.2f".format(order.totalPrice)
        binding.tvNotes.text      = order.notes.ifEmpty { "No special instructions" }
        applyStatusStyle()
    }

    private fun populateItems() {
        binding.llItems.removeAllViews()
        db.getOrderItems(order.id).forEach { item ->
            binding.llItems.addView(TextView(this).apply {
                text      = "  • ${item.quantity}x ${item.itemName}   ${"$%.2f".format(item.price * item.quantity)}"
                textSize  = 14f
                setPadding(0, 10, 0, 10)
                setTextColor(getColor(R.color.text_primary))
            })
        }
    }

    private fun applyStatusStyle() {
        binding.tvStatus.text = order.status
        val (bg, fg) = when (order.status) {
            Order.STATUS_CONFIRMED -> "#E3F2FD" to "#1E88E5"
            Order.STATUS_PREPARING -> "#FFF3E0" to "#F97316"
            Order.STATUS_READY,
            Order.STATUS_DELIVERED -> "#E8F9F1" to "#06C167"
            Order.STATUS_CANCELLED -> "#FEE2E2" to "#EF4444"
            else                   -> "#F5F5F5" to "#6B6B6B"
        }
        binding.tvStatus.setBackgroundColor(Color.parseColor(bg))
        binding.tvStatus.setTextColor(Color.parseColor(fg))
    }

    private fun showStatusDialog() {
        val statuses = arrayOf(Order.STATUS_CONFIRMED, Order.STATUS_PREPARING,
                               Order.STATUS_READY, Order.STATUS_DELIVERED)
        AlertDialog.Builder(this).setTitle("Update Status")
            .setItems(statuses) { _, i ->
                order.status = statuses[i]
                db.updateOrderStatus(order.id, statuses[i])
                applyStatusStyle()
                Snackbar.make(binding.root, "Status → ${statuses[i]}", Snackbar.LENGTH_SHORT).show()
            }.show()
    }

    private fun showEditNotesDialog() {
        val et = EditText(this).apply { setText(order.notes); setPadding(48,32,48,32) }
        AlertDialog.Builder(this).setTitle("Edit Instructions")
            .setView(et)
            .setPositiveButton("Save") { _, _ ->
                order.notes = et.text.toString().trim()
                db.updateOrderNotes(order.id, order.notes)
                binding.tvNotes.text = order.notes.ifEmpty { "No special instructions" }
                Snackbar.make(binding.root, "Instructions updated.", Snackbar.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null).show()
    }

    private fun showCancelDialog() {
        AlertDialog.Builder(this)
            .setTitle("Cancel Order")
            .setMessage("Are you sure you want to cancel this order?")
            .setPositiveButton("Yes, Cancel") { _, _ ->
                order.status = Order.STATUS_CANCELLED
                db.updateOrderStatus(order.id, Order.STATUS_CANCELLED)
                applyStatusStyle()
                binding.btnCancelOrder.visibility  = View.GONE
                binding.btnUpdateStatus.visibility = View.GONE
                Snackbar.make(binding.root, "Order cancelled.", Snackbar.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null).show()
    }
}
