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
import com.management.roomates.data.model.BillingItem
import com.management.roomates.databinding.FragmentBillingsBinding
import com.management.roomates.viewmodel.BillingsViewModel

class BillingsFragment : Fragment() {
    private var _binding: FragmentBillingsBinding? = null
    private val binding get() = _binding!!
    private val billingsViewModel: BillingsViewModel by viewModels()
    private lateinit var billingsAdapter: BillingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBillingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        billingsAdapter = BillingsAdapter(billingsViewModel)
        binding.recyclerViewBillings.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = billingsAdapter
        }

        billingsViewModel.allBillings.observe(viewLifecycleOwner, Observer { billings ->
            billingsAdapter.submitList(billings)
        })

        binding.buttonAddBilling.setOnClickListener {
            val date = binding.editTextDate.text.toString()
            val amount = binding.editTextAmount.text.toString().toDoubleOrNull() ?: 0.0
            val status = binding.editTextStatus.text.toString()
            val lastUpdatedBy = "currentUser" // Replace with actual current user
            val lastUpdatedAt = System.currentTimeMillis()

            if (date.isNotEmpty() && amount > 0 && status.isNotEmpty()) {
                val billingItem = BillingItem(
                    date = date,
                    amount = amount,
                    status = status,
                    lastUpdatedBy = lastUpdatedBy,
                    lastUpdatedAt = lastUpdatedAt
                )
                billingsViewModel.insert(billingItem)
                binding.editTextDate.text?.clear()
                binding.editTextAmount.text?.clear()
                binding.editTextStatus.text?.clear()
            } else {
                Toast.makeText(requireContext(), "Please enter valid date, amount and status", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
