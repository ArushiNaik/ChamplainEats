package com.champlain.fastfood.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.champlain.fastfood.R
import com.champlain.fastfood.models.FoodItem

class FoodItemAdapter(
    private val items: List<FoodItem>,
    private val showQty: Boolean,
    private val onQtyChanged: (() -> Unit)? = null
) : RecyclerView.Adapter<FoodItemAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvName:   TextView = v.findViewById(R.id.tv_item_name)
        val tvDesc:   TextView = v.findViewById(R.id.tv_item_desc)
        val tvPrice:  TextView = v.findViewById(R.id.tv_item_price)
        val tvCat:    TextView = v.findViewById(R.id.tv_item_cat)
        val layoutQty: View   = v.findViewById(R.id.layout_qty)
        val tvQty:    TextView = v.findViewById(R.id.tv_qty)
        val btnMinus: TextView = v.findViewById(R.id.btn_minus)
        val btnPlus:  TextView = v.findViewById(R.id.btn_plus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, vt: Int) =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(h: VH, pos: Int) {
        val item = items[pos]
        h.tvName.text  = item.name
        h.tvDesc.text  = item.description
        h.tvPrice.text = "$%.2f".format(item.price)
        h.tvCat.text   = item.category

        if (showQty) {
            h.layoutQty.visibility = View.VISIBLE
            h.tvQty.text = item.quantity.toString()
            h.btnMinus.setOnClickListener {
                if (item.quantity > 0) { item.quantity--; h.tvQty.text = item.quantity.toString(); onQtyChanged?.invoke() }
            }
            h.btnPlus.setOnClickListener {
                item.quantity++; h.tvQty.text = item.quantity.toString(); onQtyChanged?.invoke()
            }
        } else {
            h.layoutQty.visibility = View.GONE
        }
    }
}
