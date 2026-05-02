package com.champlain.fastfood.adapters

import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.champlain.fastfood.components.OrderItem
import com.champlain.fastfood.models.Order
import com.champlain.fastfood.ui.theme.ChamplainEatsTheme

class OrderAdapter(private val onClick: (Order) -> Unit) :
    ListAdapter<Order, OrderAdapter.VH>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(a: Order, b: Order) = a.id == b.id
            override fun areContentsTheSame(a: Order, b: Order) = a == b
        }
    }

    inner class VH(val composeView: ComposeView) : RecyclerView.ViewHolder(composeView)

    override fun onCreateViewHolder(parent: ViewGroup, vt: Int): VH {
        return VH(ComposeView(parent.context))
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val o = getItem(pos)
        h.composeView.setContent {
            ChamplainEatsTheme {
                OrderItem(order = o, onClick = { onClick(o) })
            }
        }
    }
}
