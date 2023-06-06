package com.example.millionairegameclient.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.millionairegameclient.databinding.RowMainBinding

class MainAdapter(
    private var onItemClicked: ((option: MainOptionsEnum) -> Unit)
): RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RowMainBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(option: MainOptionsEnum) = binding.apply {
            rowMainIcon.setImageResource(option.drawableId)
            rowMainTitle.text = option.title
            root.setOnClickListener {
                onItemClicked(option)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return MainOptionsEnum.values().size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(MainOptionsEnum.values()[position])
    }
}