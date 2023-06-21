package com.example.millionairegameclient.ui.home

import com.example.millionairegameclient.R

enum class MainOptionsEnum (
    val title: String,
    val drawableId: Int
    ) {
    LoadQuestion("Load Question", R.drawable.ic_load),
    ShowQuestion("Show Question", R.drawable.ic_main_show_question),
    ShowOption( "Show Option", R.drawable.ic_main_show_option),
    MarkOption("Mark Option", R.drawable.ic_main_mark_option),
    ShowAnswer("Show Answer", R.drawable.ic_main_show_answer),
    ShowAllOptions("Show All answers", R.drawable.ic_main_change_next),
    NavigateReward("Navigate Reward", R.drawable.ic_main_show_reward),
    NavigateClock("Navigate Clock", R.drawable.ic_clock),
    NavigateChart("Navigate Person", R.drawable.round_person),
    NavigateTable("Navigate Table", R.drawable.ic_table),
    NavigateNext("Navigate Next", R.drawable.baseline_navigate_next),
    NavigateUp("Navigate Up", R.drawable.ic_nav_up)
}