package com.champlain.fastfood.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.champlain.fastfood.R
import com.champlain.fastfood.adapters.CommentAdapter
import com.champlain.fastfood.adapters.FoodItemAdapter
import com.champlain.fastfood.database.DatabaseHelper
import com.champlain.fastfood.databinding.ActivityRestaurantDetailBinding
import com.champlain.fastfood.databinding.DialogAddReviewBinding
import com.champlain.fastfood.models.Comment
import com.google.android.material.snackbar.Snackbar

class RestaurantDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRestaurantDetailBinding
    private lateinit var db: DatabaseHelper
    private lateinit var commentAdapter: CommentAdapter
    private var restaurantId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        restaurantId = intent.getIntExtra("restaurant_id", -1)
        if (restaurantId == -1) { finish(); return }

        db = DatabaseHelper.getInstance(this)
        val r = db.getRestaurantById(restaurantId) ?: run { finish(); return }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply { setDisplayHomeAsUpEnabled(true); title = "" }
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // Banner gradient
        val drawableId = resources.getIdentifier(r.bannerDrawable, "drawable", packageName)
        if (drawableId != 0) binding.viewBanner.setBackgroundResource(drawableId)

        binding.tvEmoji.text    = r.emoji
        binding.tvName.text     = r.name
        binding.tvCategory.text = r.category
        // FIX: build string without .format() to avoid MissingFormatArgumentException
        binding.tvRatingVal.text  = "%.1f".format(r.rating)
        binding.tvRatingHours.text = "${r.openTime} – ${r.closeTime}"

        binding.tvDescription.text = r.description

        if (r.discountPercent > 0) {
            binding.tvDiscount.visibility = View.VISIBLE
            binding.tvDiscount.text = "🎓 ${r.discountPercent}% Student Discount"
        } else {
            binding.tvDiscount.visibility = View.GONE
        }
        binding.tvCampus.visibility = if (r.isCampus) View.VISIBLE else View.GONE

        // Menu
        binding.rvMenu.layoutManager = LinearLayoutManager(this)
        binding.rvMenu.adapter = FoodItemAdapter(db.getFoodItemsByRestaurant(restaurantId), showQty = false)

        // Reviews
        commentAdapter = CommentAdapter { id -> db.deleteComment(id); refreshComments() }
        binding.rvComments.layoutManager = LinearLayoutManager(this)
        binding.rvComments.adapter = commentAdapter
        refreshComments()

        binding.btnAddReview.setOnClickListener { showAddReviewDialog() }
        binding.fabOrder.setOnClickListener {
            startActivity(Intent(this, PlaceOrderActivity::class.java).putExtra("restaurant_id", restaurantId))
        }
    }

    private fun refreshComments() = commentAdapter.submitList(db.getCommentsByRestaurant(restaurantId))

    private fun showAddReviewDialog() {
        val dBinding = DialogAddReviewBinding.inflate(layoutInflater)
        AlertDialog.Builder(this)
            .setTitle("Write a Review")
            .setView(dBinding.root)
            .setPositiveButton("Submit") { _, _ ->
                val name = dBinding.etName.text.toString().trim().ifEmpty { "Anonymous" }
                val text = dBinding.etReview.text.toString().trim()
                if (text.isNotEmpty()) {
                    db.insertComment(Comment(restaurantId = restaurantId, userName = name,
                        rating = dBinding.ratingBar.rating.toInt(), commentText = text))
                    refreshComments()
                    Snackbar.make(binding.root, "Review submitted! 🎉", Snackbar.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null).show()
    }
}
