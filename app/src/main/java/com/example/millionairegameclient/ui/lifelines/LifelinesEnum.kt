package com.example.millionairegameclient.ui.lifelines

import com.example.millionairegameclient.R

enum class LifelinesEnum (
    val title: String,
    val drawableId: Int
    ) {
    ShowPeopleForm("Show people form", R.drawable.ic_group_form),
    Show50("Show 50/50", R.drawable.ic_50_50),

    TogglePhone("Toggle Phone", R.drawable.ic_phone),
    Toggle50("Toggle 50/50", R.drawable.ic_balance),
    ToggleGroup("Toggle group", R.drawable.outline_groups),
    ToggleChart("Toggle Chart", R.drawable.chart)
}