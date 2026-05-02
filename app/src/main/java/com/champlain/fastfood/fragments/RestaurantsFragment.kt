package com.champlain.fastfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.champlain.fastfood.activities.RestaurantDetailActivity
import com.champlain.fastfood.database.DatabaseHelper
import com.champlain.fastfood.ui.screens.AllRestaurantsScreen
import com.champlain.fastfood.ui.theme.ChamplainEatsTheme

class RestaurantsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val db = DatabaseHelper.getInstance(requireContext())
        val restaurantsState = mutableStateListOf<com.champlain.fastfood.models.Restaurant>().apply {
            addAll(db.getAllRestaurants())
        }

        return ComposeView(requireContext()).apply {
            setContent {
                ChamplainEatsTheme {
                    AllRestaurantsScreen(
                        restaurants = restaurantsState,
                        onSearch = { query ->
                            restaurantsState.clear()
                            restaurantsState.addAll(
                                if (query.isEmpty()) db.getAllRestaurants()
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
