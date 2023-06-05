package com.example.millionairegameclient.ui.bluetooth

object Actions {
    private const val prefix = "millgame."
    const val START_FOREGROUND = prefix + "startforeground"
    const val STOP_FOREGROUND = prefix + "stopforeground"
    const val DISCONNECT_DEVICE = prefix + "disconnectdevice"
    const val CLOSE_CONNECTION = prefix + "closeconn"
    const val MAIN_SHOW_QUESTION = prefix + "showquestion"
    const val MAIN_SHOW_OPTION = prefix + "showopt"
    const val MAIN_MARK_OPTION = prefix + "markopt"
    const val MAIN_SHOW_ANSWER = prefix + "showans"
    const val MAIN_SHOW_REWARD = prefix + "showrew"
    const val MAIN_CHANGE_NEXT_Q = prefix + "changenext"
    const val LIFE_SHOW_PPL_FORM = prefix + "showpplform"
    const val LIFE_SHOW_PPL_CHOICE = prefix + "showpplchoice"
    const val LIFE_SHOW_CLOCK = prefix + "showclock"
    const val LIFE_SHOW_50 = prefix + "show50"
    const val LIFE_TOGGLE = prefix + "togglelife"
    const val CONFIG_SHOW_OPENING = prefix + "showop"
    const val CONFIG_SHOW_TABLE = prefix + "showtable"
    const val CONFIG_SELECT_QUEST = prefix + "selectquest"
}