package com.champlain.fastfood

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.champlain.fastfood.databinding.ActivityMainBinding
import com.champlain.fastfood.fragments.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) loadFragment(HomeFragment())

        binding.bottomNav.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_home        -> HomeFragment()
                R.id.nav_restaurants -> RestaurantsFragment()
                R.id.nav_orders      -> OrdersFragment()
                R.id.nav_profile     -> ProfileFragment()
                else                 -> HomeFragment()
            }
            loadFragment(fragment)
            true
        }
    }

    private fun loadFragment(f: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, f)
            .commit()
    }
}
