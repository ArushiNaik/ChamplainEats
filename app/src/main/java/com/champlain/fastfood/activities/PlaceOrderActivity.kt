package com.champlain.fastfood.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.champlain.fastfood.adapters.FoodItemAdapter
import com.champlain.fastfood.database.DatabaseHelper
import com.champlain.fastfood.databinding.ActivityPlaceOrderBinding
import com.champlain.fastfood.models.Order
import com.google.android.material.snackbar.Snackbar

class PlaceOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaceOrderBinding
    private lateinit var db: DatabaseHelper
    private var discountPercent = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val restaurantId = intent.getIntExtra("restaurant_id", -1)
        if (restaurantId == -1) { finish(); return }

        db = DatabaseHelper.getInstance(this)
        val restaurant = db.getRestaurantById(restaurantId) ?: run { finish(); return }
        discountPercent = restaurant.discountPercent

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = restaurant.name
        }
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        if (discountPercent > 0) {
            binding.tvDiscountLabel.text = "Student Discount ($discountPercent%)"
        } else {
            binding.tvDiscountLabel.text = "Student Discount"
        }

        val foodItems = db.getFoodItemsByRestaurant(restaurantId)
        binding.rvItems.layoutManager = LinearLayoutManager(this)
        binding.rvItems.adapter = FoodItemAdapter(foodItems, showQty = true) {
            updateSummary(foodItems.sumOf { it.price * it.quantity })
        }
        updateSummary(0.0)

        binding.btnPlaceOrder.setOnClickListener {
            val selected = foodItems.filter { it.quantity > 0 }
            if (selected.isEmpty()) {
                Snackbar.make(binding.root, "Please add at least one item.", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val orderType = if (binding.rbDelivery.isChecked) Order.TYPE_DELIVERY else Order.TYPE_PICKUP
            val notes     = binding.etNotes.text.toString().trim()
            val summary   = selected.joinToString(", ") { "${it.quantity}x ${it.name}" }
            val subtotal  = selected.sumOf { it.price * it.quantity }
            val total     = subtotal - subtotal * discountPercent / 100.0

            val orderId = db.insertOrder(Order(
                restaurantId   = restaurantId,
                restaurantName = restaurant.name,
                orderType      = orderType,
                status         = Order.STATUS_PENDING,
                totalPrice     = total,
                notes          = notes,
                itemsSummary   = summary
            ))
            db.insertOrderItems(orderId, selected)
            Snackbar.make(binding.root, "Order placed successfully! 🎉", Snackbar.LENGTH_LONG).show()
            finish()
        }
    }

    private fun updateSummary(subtotal: Double) {
        val discount = subtotal * discountPercent / 100.0
        binding.tvSubtotal.text    = "$%.2f".format(subtotal)
        binding.tvDiscountAmt.text = "-$%.2f".format(discount)
        binding.tvTotal.text       = "$%.2f".format(subtotal - discount)
    }
}
