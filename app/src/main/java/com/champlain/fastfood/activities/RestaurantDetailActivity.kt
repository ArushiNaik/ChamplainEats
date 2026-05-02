package com.champlain.fastfood.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.compose.runtime.mutableStateListOf
import com.champlain.fastfood.database.DatabaseHelper
import com.champlain.fastfood.databinding.DialogAddReviewBinding
import com.champlain.fastfood.models.Comment
import com.champlain.fastfood.ui.screens.RestaurantDetailScreen
import com.champlain.fastfood.ui.theme.ChamplainEatsTheme
import com.google.android.material.snackbar.Snackbar

class RestaurantDetailActivity : ComponentActivity() {

    private lateinit var db: DatabaseHelper
    private var restaurantId = -1
    private val commentsState = mutableStateListOf<Comment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        restaurantId = intent.getIntExtra("restaurant_id", -1)
        if (restaurantId == -1) { finish(); return }

        db = DatabaseHelper.getInstance(this)
        val r = db.getRestaurantById(restaurantId) ?: run { finish(); return }
        val menuItems = db.getFoodItemsByRestaurant(restaurantId)
        
        refreshComments()

        setContent {
            ChamplainEatsTheme {
                RestaurantDetailScreen(
                    restaurant = r,
                    menuItems = menuItems,
                    comments = commentsState,
                    onBackClick = { onBackPressedDispatcher.onBackPressed() },
                    onAddReviewClick = { showAddReviewDialog() },
                    onDeleteComment = { id -> 
                        db.deleteComment(id)
                        refreshComments()
                    },
                    onOrderNowClick = {
                        startActivity(Intent(this, PlaceOrderActivity::class.java).putExtra("restaurant_id", restaurantId))
                    }
                )
            }
        }
    }

    private fun refreshComments() {
        commentsState.clear()
        commentsState.addAll(db.getCommentsByRestaurant(restaurantId))
    }

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
                    Snackbar.make(window.decorView.rootView, "Review submitted! 🎉", Snackbar.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null).show()
    }
}
