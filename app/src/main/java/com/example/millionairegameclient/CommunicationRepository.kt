package com.example.millionairegameclient

import android.content.Context
import android.view.View
import com.example.millionairegameclient.ui.bluetooth.Actions
import com.example.millionairegameclient.ui.home.MainOptionsEnum
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

    private fun sendAction(action: String) {

    }
}