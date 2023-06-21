package com.example.millionairegameclient.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.millionairegameclient.R
import com.example.millionairegameclient.databinding.RowMainBinding

class MainAdapter(
    private val context: Context,
    private var onItemClicked: ((option: MainOptionsEnum) -> Unit)
): RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RowMainBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(option: MainOptionsEnum) = binding.apply {
            rowMainIcon.setImageResource(option.drawableId)
            rowMainTitle.text = option.title
            root.setOnClickListener {
                onItemClicked(option)
            }
            when (option) {
                MainOptionsEnum.LoadQuestion -> binding.cardview.setCardBackgroundColor(context.resources.getColor(R.color.teal_700))
                MainOptionsEnum.ShowQuestion -> binding.cardview.setCardBackgroundColor(context.resources.getColor(R.color.purple_200))
                MainOptionsEnum.ShowOption -> binding.cardview.setCardBackgroundColor(context.resources.getColor(R.color.white))
                MainOptionsEnum.MarkOption -> binding.cardview.setCardBackgroundColor(context.resources.getColor(R.color.reward_bg))
                MainOptionsEnum.ShowAnswer -> binding.cardview.setCardBackgroundColor(context.resources.getColor(R.color.answer))
                MainOptionsEnum.ShowAllOptions -> binding.cardview.setCardBackgroundColor(context.resources.getColor(R.color.gray))
                MainOptionsEnum.NavigateReward -> binding.cardview.setCardBackgroundColor(context.resources.getColor(R.color.yellow))
                MainOptionsEnum.NavigateClock -> binding.cardview.setCardBackgroundColor(context.resources.getColor(R.color.yellow))
                MainOptionsEnum.NavigateChart -> binding.cardview.setCardBackgroundColor(context.resources.getColor(R.color.yellow))
                MainOptionsEnum.NavigateTable -> binding.cardview.setCardBackgroundColor(context.resources.getColor(R.color.yellow))
                MainOptionsEnum.NavigateNext -> binding.cardview.setCardBackgroundColor(context.resources.getColor(R.color.yellow))
                MainOptionsEnum.NavigateUp -> binding.cardview.setCardBackgroundColor(context.resources.getColor(R.color.pink))
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