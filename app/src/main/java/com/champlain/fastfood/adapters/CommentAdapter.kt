package com.champlain.fastfood.adapters

import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.champlain.fastfood.components.CommentItem
import com.champlain.fastfood.models.Comment
import com.champlain.fastfood.ui.theme.ChamplainEatsTheme

class CommentAdapter(private val onDelete: (Int) -> Unit) :
    ListAdapter<Comment, CommentAdapter.VH>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(a: Comment, b: Comment) = a.id == b.id
            override fun areContentsTheSame(a: Comment, b: Comment) = a == b
        }
    }

    inner class VH(val composeView: ComposeView) : RecyclerView.ViewHolder(composeView)

    override fun onCreateViewHolder(parent: ViewGroup, vt: Int): VH {
        return VH(ComposeView(parent.context))
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val c = getItem(pos)
        h.composeView.setContent {
            ChamplainEatsTheme {
                CommentItem(comment = c, onDelete = onDelete)
            }
        }
    }
}
