package com.champlain.fastfood.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.champlain.fastfood.R
import com.champlain.fastfood.models.Comment

class CommentAdapter(private val onDelete: (Int) -> Unit) :
    ListAdapter<Comment, CommentAdapter.VH>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(a: Comment, b: Comment) = a.id == b.id
            override fun areContentsTheSame(a: Comment, b: Comment) = a == b
        }
    }

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvAvatar:    TextView    = v.findViewById(R.id.tv_comment_user)
        val tvUserName:  TextView    = v.findViewById(R.id.tv_comment_user_name)
        val tvText:      TextView    = v.findViewById(R.id.tv_comment_text)
        val tvDate:      TextView    = v.findViewById(R.id.tv_comment_date)
        val tvRating:    TextView    = v.findViewById(R.id.tv_comment_rating)
        val btnDel:      ImageButton = v.findViewById(R.id.btn_delete_comment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, vt: Int) =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false))

    override fun onBindViewHolder(h: VH, pos: Int) {
        val c = getItem(pos)
        h.tvAvatar.text   = c.userName.firstOrNull()?.uppercaseChar()?.toString() ?: "A"
        h.tvUserName.text = c.userName
        h.tvText.text     = c.commentText
        h.tvDate.text     = c.createdAt
        h.tvRating.text   = "★".repeat(c.rating) + "☆".repeat(5 - c.rating)
        h.btnDel.setOnClickListener { onDelete(c.id) }
    }
}
