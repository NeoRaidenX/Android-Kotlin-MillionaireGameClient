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
    ShowReward("Show Reward", R.drawable.ic_main_show_reward),
    ChangeNext("Next Question", R.drawable.ic_main_change_next),
    NavigateUp("Navigate Up", R.drawable.ic_nav_up)
}