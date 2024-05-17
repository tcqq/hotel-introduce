package com.management.roomates.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.management.roomates.data.model.GroceryItem
import com.management.roomates.databinding.FragmentGroceriesBinding
import com.management.roomates.viewmodel.GroceriesViewModel

class GroceriesFragment : Fragment() {
    private var _binding: FragmentGroceriesBinding? = null
    private val binding get() = _binding!!
    private val groceriesViewModel: GroceriesViewModel by viewModels()
    private lateinit var groceriesAdapter: GroceriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroceriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        groceriesAdapter = GroceriesAdapter(groceriesViewModel)
        binding.recyclerViewGroceries.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = groceriesAdapter
        }

        groceriesViewModel.allGroceries.observe(viewLifecycleOwner, Observer { groceries ->
            groceriesAdapter.submitList(groceries)
        })

        binding.buttonAddGrocery.setOnClickListener {
            val itemName = binding.editTextItemName.text.toString()
            val quantity = binding.editTextQuantity.text.toString().toIntOrNull() ?: 0
            val lastUpdatedBy = "currentUser" // Replace with actual current user
            val lastUpdatedAt = System.currentTimeMillis()

            if (itemName.isNotEmpty() && quantity > 0) {
                val groceryItem = GroceryItem(
                    name = itemName,
                    quantity = quantity,
                    lastUpdatedBy = lastUpdatedBy,
                    lastUpdatedAt = lastUpdatedAt
                )
                groceriesViewModel.insert(groceryItem)
                binding.editTextItemName.text?.clear()
                binding.editTextQuantity.text?.clear()
            } else {
                Toast.makeText(requireContext(), "Please enter valid item name and quantity", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
