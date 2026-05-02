package com.champlain.fastfood.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.champlain.fastfood.R
import com.champlain.fastfood.models.Order

class OrderAdapter(private val onClick: (Order) -> Unit) :
    ListAdapter<Order, OrderAdapter.VH>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(a: Order, b: Order) = a.id == b.id
            override fun areContentsTheSame(a: Order, b: Order) = a == b
        }
    }

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvRestaurant: TextView = v.findViewById(R.id.tv_order_restaurant)
        val tvItems:      TextView = v.findViewById(R.id.tv_order_items)
        val tvTotal:      TextView = v.findViewById(R.id.tv_order_total)
        val tvDate:       TextView = v.findViewById(R.id.tv_order_date)
        val tvType:       TextView = v.findViewById(R.id.tv_order_type)
        val tvStatus:     TextView = v.findViewById(R.id.tv_order_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, vt: Int) =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false))

    override fun onBindViewHolder(h: VH, pos: Int) {
        val o = getItem(pos)
        h.tvRestaurant.text = o.restaurantName
        h.tvItems.text      = o.itemsSummary
        h.tvTotal.text      = "$%.2f".format(o.totalPrice)
        h.tvDate.text       = o.createdAt
        h.tvType.text       = if (o.orderType == Order.TYPE_DELIVERY) "🛵 Delivery" else "🛍️ Pickup"
        h.tvStatus.text     = o.status
        val (bg, fg) = statusColors(o.status)
        h.tvStatus.setBackgroundColor(Color.parseColor(bg))
        h.tvStatus.setTextColor(Color.parseColor(fg))
        h.itemView.setOnClickListener { onClick(o) }
    }

    private fun statusColors(s: String) = when(s) {
        Order.STATUS_CONFIRMED -> "#E3F2FD" to "#1E88E5"
        Order.STATUS_PREPARING -> "#FFF3E0" to "#F97316"
        Order.STATUS_READY,
        Order.STATUS_DELIVERED -> "#E8F9F1" to "#06C167"
        Order.STATUS_CANCELLED -> "#FEE2E2" to "#EF4444"
        else                   -> "#F5F5F5" to "#6B6B6B"
    }
}
