package com.champlain.fastfood.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.champlain.fastfood.database.DatabaseHelper
import com.champlain.fastfood.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = DatabaseHelper.getInstance(requireContext())
        binding.tvOrderCount.text = db.getOrderCount().toString()
        binding.tvTotalSaved.text = "$%.2f".format(db.getTotalSaved())
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
