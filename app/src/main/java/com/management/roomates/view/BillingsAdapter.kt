package com.management.roomates.view
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.management.roomates.data.model.BillingItem
import com.management.roomates.databinding.ItemBillingBinding
import com.management.roomates.viewmodel.BillingsViewModel

class BillingsAdapter(private val billingsViewModel: BillingsViewModel) :
    ListAdapter<BillingItem, BillingsAdapter.BillingViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingViewHolder {
        val binding = ItemBillingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BillingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BillingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BillingViewHolder(private val binding: ItemBillingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.buttonDelete.setOnClickListener {
                val item = getItem(adapterPosition)
                billingsViewModel.delete(item)
            }
        }

        fun bind(billingItem: BillingItem) {
            binding.item = billingItem
            binding.executePendingBindings()
        }
    }
    class DiffCallback : DiffUtil.ItemCallback<BillingItem>() {
        override fun areItemsTheSame(oldItem: BillingItem, newItem: BillingItem): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: BillingItem, newItem: BillingItem): Boolean {
            return oldItem == newItem
        }
    }
}
