package com.felipemz.inventaryapp.core.base

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : State, E : Event> : ViewModel() {

    private val intents: Channel<E> = Channel(Channel.BUFFERED)

    private val _state: MutableStateFlow<S> by lazy { MutableStateFlow(initState()) }
    val state get() = _state.asStateFlow()

    abstract fun initState(): S

    abstract fun intentHandler()

    fun eventHandler(intent: E) {
        execute(Dispatchers.IO) { intents.send(intent) }
    }

    protected fun updateState(
        reducer: suspend (intent: S) -> S
    ) = execute { _state.update { reducer(it) } }

    protected fun executeIntent(
        action: suspend (E) -> Unit
    ) = execute { intents.consumeAsFlow().collect { action(it) } }

    protected fun execute(
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        action: suspend CoroutineScope.() -> Unit,
    ) = viewModelScope.launch(dispatcher) { action() }
}

interface Event

interface Action

@Stable
interface State