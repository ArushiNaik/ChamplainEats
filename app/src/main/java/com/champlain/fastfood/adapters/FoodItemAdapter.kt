package com.champlain.fastfood.adapters

import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.RecyclerView
import com.champlain.fastfood.components.FoodItemComponent
import com.champlain.fastfood.models.FoodItem
import com.champlain.fastfood.ui.theme.ChamplainEatsTheme

class FoodItemAdapter(
    private val items: List<FoodItem>,
    private val showQty: Boolean,
    private val onQtyChanged: (() -> Unit)? = null
) : RecyclerView.Adapter<FoodItemAdapter.VH>() {

    inner class VH(val composeView: ComposeView) : RecyclerView.ViewHolder(composeView)

    override fun onCreateViewHolder(parent: ViewGroup, vt: Int): VH {
        return VH(ComposeView(parent.context))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(h: VH, pos: Int) {
        val item = items[pos]
        h.composeView.setContent {
            ChamplainEatsTheme {
                FoodItemComponent(
                    item = item,
                    showQty = showQty,
                    onQtyChanged = { onQtyChanged?.invoke() }
                )
            }
        }
    }
}
