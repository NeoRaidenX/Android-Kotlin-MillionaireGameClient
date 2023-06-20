package com.example.millionairegameclient.ui.settings

import com.example.millionairegameclient.R

enum class SettingsEnum (
    val title: String,
    val drawableId: Int
    ) {
    ShowOpening("Show opening", R.drawable.ic_opening),
    SelectCurrent("Select current question", R.drawable.ic_question),
    ShowCurrentQuestion("Navigate to question", R.drawable.ic_dashboard_black_24dp)

}