package com.champlain.fastfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.champlain.fastfood.activities.RestaurantDetailActivity
import com.champlain.fastfood.database.DatabaseHelper
import com.champlain.fastfood.models.Restaurant
import com.champlain.fastfood.ui.screens.HomeScreen
import com.champlain.fastfood.ui.theme.ChamplainEatsTheme

class HomeFragment : Fragment() {

    private lateinit var db: DatabaseHelper
    private val categories = listOf("All", "Café", "Burgers", "Pizza", "Japanese", "Mexican", "Mediterranean", "Healthy", "Asian")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        db = DatabaseHelper.getInstance(requireContext())

        return ComposeView(requireContext()).apply {
            setContent {
                ChamplainEatsTheme {
                    var selectedCategory by remember { mutableStateOf("All") }
                    val restaurantsState = remember { mutableStateListOf<Restaurant>() }
                    
                    // Initial load
                    LaunchedEffect(selectedCategory) {
                        restaurantsState.clear()
                        restaurantsState.addAll(db.getRestaurantsByCategory(selectedCategory))
                    }

                    HomeScreen(
                        restaurants = restaurantsState,
                        categories = categories,
                        selectedCategory = selectedCategory,
                        onCategorySelected = { category ->
                            selectedCategory = category
                        },
                        onSearch = { query ->
                            restaurantsState.clear()
                            restaurantsState.addAll(
                                if (query.isEmpty()) db.getRestaurantsByCategory(selectedCategory)
                                else db.searchRestaurants(query)
                            )
                        },
                        onRestaurantClick = { restaurant ->
                            startActivity(
                                Intent(requireContext(), RestaurantDetailActivity::class.java)
                                    .putExtra("restaurant_id", restaurant.id)
                            )
                        }
                    )
                }
            }
        }
    }
}
