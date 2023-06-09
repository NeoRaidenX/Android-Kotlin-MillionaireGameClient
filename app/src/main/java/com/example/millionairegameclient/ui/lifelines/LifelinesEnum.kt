package com.example.millionairegameclient.ui.lifelines

import com.example.millionairegameclient.R

enum class LifelinesEnum (
    val title: String,
    val drawableId: Int
    ) {
    ShowPeopleForm("Show people form", R.drawable.ic_group_form),
    ShowPeopleDiagram("Show chart", R.drawable.ic_chart),
    ShowTimer("Show timer", R.drawable.ic_clock),
    Show50("Show 50/50", R.drawable.ic_50_50),
    TogglePeopleForm("Toggle people form", 0),
    TogglePeopleDiagram("Toggle people chart", 0),
    ToggleTimer("Toggle timer", 0),
    Toggle50("Toggle 50/50", 0)
}