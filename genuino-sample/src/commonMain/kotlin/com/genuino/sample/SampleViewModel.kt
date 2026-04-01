package com.genuino.sample

import com.genuino.sdk.contracts.GenuinoActionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class SampleViewModel : GenuinoActionHandler {
    private val _actions = MutableSharedFlow<String>()
    val actions: SharedFlow<String> = _actions.asSharedFlow()

    override fun onAction(actionId: String, payload: Map<String, String>) {
        // Here you would process the business logic
        // For demonstration, we just emit a message
        val message = "Ação interceptada no ViewModel: $actionId com dados $payload"
        // In a real app, we might update a state or trigger a side effect
        // Using a flow to communicate back to the UI for the Snackbar
        kotlinx.coroutines.MainScope().run {
            _actions.tryEmit(message)
        }
    }
}
