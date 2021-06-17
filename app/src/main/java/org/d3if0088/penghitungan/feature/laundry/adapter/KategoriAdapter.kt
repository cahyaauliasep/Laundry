package org.d3if0088.penghitungan.feature.laundry.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.d3if0088.penghitungan.databinding.ItemKategoriBinding
import org.d3if0088.penghitungan.db.LaundryEntity

enum class LaundryClickAction { UPDATE, DELETE }
class LaundryListener(val clickListener: (laundry: LaundryEntity, action: LaundryClickAction) -> Unit) {
    fun onClick(laundry: LaundryEntity, action: LaundryClickAction) = clickListener(laundry, action)
}

class KategoriAdapter(private val clickListener: LaundryListener) : RecyclerView.Adapter<KategoriAdapter.ViewHolder>() {
    private val data = mutableListOf<LaundryEntity>()
    fun updateData(newData: List<LaundryEntity>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemKategoriBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], clickListener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(
        private val binding: ItemKategoriBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LaundryEntity, clickListener: LaundryListener) = with(binding) {
            binding.kategoriTextView.text = item.kategori
            binding.container.setOnClickListener {
                clickListener.onClick(item, LaundryClickAction.UPDATE)
            }
            binding.deleteUserID.setOnClickListener {
                clickListener.onClick(item, LaundryClickAction.DELETE)
            }
        }
    }
}