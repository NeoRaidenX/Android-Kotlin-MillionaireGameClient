package com.example.millionairegameclient

import android.content.Context
import android.view.View
import com.example.millionairegameclient.ui.bluetooth.Actions
import com.example.millionairegameclient.ui.home.MainOptionsEnum
import com.example.millionairegameclient.ui.lifelines.LifelinesEnum
import com.example.millionairegameclient.ui.settings.SettingsEnum
import com.google.android.material.snackbar.Snackbar

class CommunicationRepository {
    suspend fun sendMainAction(context: Context, view: View, option: MainOptionsEnum) {
        Snackbar.make(context, view, option.title, Snackbar.LENGTH_SHORT).show()
        when (option) {
            MainOptionsEnum.ShowQuestion -> {
                sendAction(Actions.MAIN_SHOW_QUESTION)
            }
            MainOptionsEnum.ShowOption -> {
                sendAction(Actions.MAIN_SHOW_OPTION)
            }
            MainOptionsEnum.MarkOption -> {
                sendAction(Actions.MAIN_MARK_OPTION)
            }
            MainOptionsEnum.ShowAnswer -> {
                sendAction(Actions.MAIN_SHOW_ANSWER)
            }
            MainOptionsEnum.ShowReward -> {
                sendAction(Actions.MAIN_SHOW_REWARD)
            }
            MainOptionsEnum.ChangeNext -> {
                sendAction(Actions.MAIN_CHANGE_NEXT_Q)
            }
        }
    }

    suspend fun sendLifelineAction(context: Context, view: View, option: LifelinesEnum) {
        Snackbar.make(context, view, option.title, Snackbar.LENGTH_SHORT).show()
        when (option) {
            LifelinesEnum.ShowPeopleForm -> {
                sendAction(Actions.LIFE_SHOW_PPL_FORM)
            }
            LifelinesEnum.ShowPeopleDiagram -> {
                sendAction(Actions.LIFE_SHOW_PPL_CHOICE)
            }
            LifelinesEnum.ShowTimer -> {
                sendAction(Actions.LIFE_SHOW_CLOCK)
            }
            LifelinesEnum.Show50 -> {
                sendAction(Actions.LIFE_SHOW_50)
            }
            LifelinesEnum.TogglePeopleForm -> {
                sendAction(Actions.LIFE_TOGGLE_FORM)
            }
            LifelinesEnum.TogglePeopleDiagram -> {
                sendAction(Actions.LIFE_TOGGLE_CHART)
            }
            LifelinesEnum.ToggleTimer -> {
                sendAction(Actions.LIFE_TOGGLE_TIMER)
            }
            LifelinesEnum.Toggle50 -> {
                sendAction(Actions.LIFE_TOGGLE_50)
            }
        }
    }

    suspend fun sendSettingsAction(context: Context, view: View, option: SettingsEnum) {
        Snackbar.make(context, view, option.title, Snackbar.LENGTH_SHORT).show()
        when (option) {
            SettingsEnum.ShowOpening -> {
                sendAction(Actions.CONFIG_SHOW_OPENING)
            }
            SettingsEnum.ShowTable -> {
                sendAction(Actions.CONFIG_SHOW_TABLE)
            }
            SettingsEnum.SelectCurrent -> {
                sendAction(Actions.CONFIG_SELECT_QUEST)
            }
        }
    }

    private fun sendAction(action: String) {

    }
}