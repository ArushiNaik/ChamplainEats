package com.champlain.fastfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.champlain.fastfood.R
import com.champlain.fastfood.activities.RestaurantDetailActivity
import com.champlain.fastfood.adapters.RestaurantAdapter
import com.champlain.fastfood.database.DatabaseHelper
import com.champlain.fastfood.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RestaurantAdapter
    private lateinit var db: DatabaseHelper
    private var selectedCategory = "All"

    private val categories = listOf("All","Café","Burgers","Pizza","Japanese","Mexican","Mediterranean","Healthy","Asian")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
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

        buildChips()

        binding.etSearch.addTextChangedListener { s ->
            val q = s?.toString() ?: ""
            adapter.submitList(if (q.isEmpty()) db.getRestaurantsByCategory(selectedCategory)
                               else db.searchRestaurants(q))
        }
    }

    private fun buildChips() {
        binding.chipContainer.removeAllViews()
        categories.forEach { cat ->
            val selected = cat == selectedCategory
            val chip = TextView(requireContext()).apply {
                text = cat
                textSize = 13f
                isSelected = selected
                background = requireContext().getDrawable(R.drawable.bg_chip_selector)
                setTextColor(if (selected)
                    requireContext().getColor(R.color.white)
                else requireContext().getColor(R.color.text_primary))
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
                lp.marginEnd = 10
                layoutParams = lp
                setOnClickListener {
                    selectedCategory = cat
                    buildChips()
                    adapter.submitList(db.getRestaurantsByCategory(cat))
                }
            }
            binding.chipContainer.addView(chip)
        }
    }

    override fun onResume() {
        super.onResume()
        if (::adapter.isInitialized) adapter.submitList(db.getRestaurantsByCategory(selectedCategory))
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
