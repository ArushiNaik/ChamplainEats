package com.champlain.fastfood.adapters

import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.champlain.fastfood.components.RestaurantItem
import com.champlain.fastfood.models.Restaurant
import com.champlain.fastfood.ui.theme.ChamplainEatsTheme

class RestaurantAdapter(private val onClick: (Restaurant) -> Unit) :
    ListAdapter<Restaurant, RestaurantAdapter.VH>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Restaurant>() {
            override fun areItemsTheSame(a: Restaurant, b: Restaurant) = a.id == b.id
            override fun areContentsTheSame(a: Restaurant, b: Restaurant) = a == b
        }
    }

    inner class VH(val composeView: ComposeView) : RecyclerView.ViewHolder(composeView)

    override fun onCreateViewHolder(parent: ViewGroup, vt: Int): VH {
        return VH(ComposeView(parent.context))
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val r = getItem(pos)
        h.composeView.setContent {
            ChamplainEatsTheme {
                RestaurantItem(restaurant = r, onClick = { onClick(r) })
            }
        }
    }
}
