package com.champlain.fastfood.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.champlain.fastfood.R
import com.champlain.fastfood.models.Restaurant

class RestaurantAdapter(private val onClick: (Restaurant) -> Unit) :
    ListAdapter<Restaurant, RestaurantAdapter.VH>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Restaurant>() {
            override fun areItemsTheSame(a: Restaurant, b: Restaurant) = a.id == b.id
            override fun areContentsTheSame(a: Restaurant, b: Restaurant) = a == b
        }
    }

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val banner:      View     = v.findViewById(R.id.view_banner)
        val tvEmoji:     TextView = v.findViewById(R.id.tv_emoji)
        val tvRating:    TextView = v.findViewById(R.id.tv_rating)
        val tvName:      TextView = v.findViewById(R.id.tv_name)
        val tvMeta:      TextView = v.findViewById(R.id.tv_meta)
        val tvDiscount:  TextView = v.findViewById(R.id.tv_discount)
        val tvCampus:    TextView = v.findViewById(R.id.tv_campus)
        val tvDelivery:  TextView = v.findViewById(R.id.tv_delivery)
    }

    override fun onCreateViewHolder(parent: ViewGroup, vt: Int) =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant, parent, false))

    override fun onBindViewHolder(h: VH, pos: Int) {
        val r = getItem(pos)
        val ctx = h.itemView.context

        // Set gradient banner
        val drawableId = ctx.resources.getIdentifier(r.bannerDrawable, "drawable", ctx.packageName)
        if (drawableId != 0) h.banner.setBackgroundResource(drawableId)
        else h.banner.setBackgroundResource(R.drawable.bg_burger)

        h.tvEmoji.text   = r.emoji
        h.tvRating.text  = "%.1f ★".format(r.rating)
        h.tvName.text    = r.name
        h.tvMeta.text    = "${r.category}  •  ${r.openTime}–${r.closeTime}"
        h.tvDelivery.text = "${r.deliveryMins}–${r.deliveryMins + 10} min"

        if (r.discountPercent > 0) {
            h.tvDiscount.visibility = View.VISIBLE
            h.tvDiscount.text = "${r.discountPercent}% off"
        } else h.tvDiscount.visibility = View.GONE

        h.tvCampus.visibility = if (r.isCampus) View.VISIBLE else View.GONE

        h.itemView.setOnClickListener { onClick(r) }
    }
}
