package com.champlain.fastfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.champlain.fastfood.activities.RestaurantDetailActivity
import com.champlain.fastfood.adapters.RestaurantAdapter
import com.champlain.fastfood.database.DatabaseHelper
import com.champlain.fastfood.databinding.FragmentRestaurantsBinding

class RestaurantsFragment : Fragment() {
    private var _binding: FragmentRestaurantsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RestaurantAdapter
    private lateinit var db: DatabaseHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRestaurantsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = DatabaseHelper.getInstance(requireContext())
        adapter = RestaurantAdapter { r ->
            startActivity(Intent(requireContext(), RestaurantDetailActivity::class.java)
                .putExtra("restaurant_id", r.id))
        }
        binding.rvRestaurants.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRestaurants.adapter = adapter
        adapter.submitList(db.getAllRestaurants())

        binding.etSearch.addTextChangedListener { s ->
            val q = s?.toString() ?: ""
            adapter.submitList(if (q.isEmpty()) db.getAllRestaurants() else db.searchRestaurants(q))
        }
    }

    override fun onResume() { super.onResume(); adapter.submitList(db.getAllRestaurants()) }
    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
