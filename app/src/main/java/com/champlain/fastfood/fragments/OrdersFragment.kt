package com.champlain.fastfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.champlain.fastfood.activities.OrderDetailActivity
import com.champlain.fastfood.activities.PlaceOrderActivity
import com.champlain.fastfood.adapters.OrderAdapter
import com.champlain.fastfood.database.DatabaseHelper
import com.champlain.fastfood.databinding.FragmentOrdersBinding

class OrdersFragment : Fragment() {
    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: OrderAdapter
    private lateinit var db: DatabaseHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = DatabaseHelper.getInstance(requireContext())

        adapter = OrderAdapter { order ->
            startActivity(Intent(requireContext(), OrderDetailActivity::class.java)
                .putExtra("order_id", order.id))
        }
        binding.rvOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOrders.adapter = adapter
        refreshOrders()

        binding.fabNewOrder.setOnClickListener { showPickRestaurantDialog() }
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
        val orders = db.getAllOrders()
        adapter.submitList(orders)
        binding.tvEmpty.visibility = if (orders.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onResume() { super.onResume(); refreshOrders() }
    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
