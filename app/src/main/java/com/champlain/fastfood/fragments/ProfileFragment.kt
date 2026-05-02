package com.champlain.fastfood.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.champlain.fastfood.database.DatabaseHelper
import com.champlain.fastfood.ui.screens.ProfileScreen
import com.champlain.fastfood.ui.theme.ChamplainEatsTheme

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val db = DatabaseHelper.getInstance(requireContext())
        
        return ComposeView(requireContext()).apply {
            setContent {
                ChamplainEatsTheme {
                    ProfileScreen(
                        orderCount = db.getOrderCount(),
                        totalSaved = db.getTotalSaved()
                    )
                }
            }
        }
    }
}
