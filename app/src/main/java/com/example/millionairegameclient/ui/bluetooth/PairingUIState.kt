package com.example.millionairegameclient.ui.bluetooth

import androidx.annotation.StringRes

sealed class PairingUIState<out String> {
    /**
     * Indicates the operation succeeded.
     */
    object Success : PairingUIState<Nothing>()

    /**
     * Indicates the operation is going on with a loading message.
     *
     * @param loadingMessage The loading message to show user.
     */
    class Loading(val loadingMessage: String) : PairingUIState<String>()

    /**
     * Indicates the operation failed with an error message.
     *
     * @param errorMessage The error message to show user.
     */
    class Error(val errorMessage: String) : PairingUIState<String>()
}
