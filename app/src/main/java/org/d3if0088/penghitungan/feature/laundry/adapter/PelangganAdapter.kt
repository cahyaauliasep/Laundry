package org.d3if0088.penghitungan.feature.laundry.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.d3if0088.penghitungan.databinding.ItemPelangganBinding
import org.d3if0088.penghitungan.feature.laundry.model.Data

class PelangganAdapter : RecyclerView.Adapter<PelangganAdapter.ViewHolder>() {
    private val data = mutableListOf<Data>()
    fun updateData(newData: List<Data>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPelangganBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }
    override fun getItemCount(): Int {
        return data.size
    }
    class ViewHolder(
        private val binding: ItemPelangganBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Data) = with(binding) {
            namaPelanggan.text = item.first_name + " " + item.last_name
        }
    }
}
