package com.book.base.mvi

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<State : IState, Event : IEvent, Action : IAction> : ViewModel() {

    /**
     * 数据处理完成后的回调
     */
    private val _uiState: MutableLiveData<State> = MutableLiveData()

    /**
     * 弹toast、跳转等一次性的页面行为
     */
    private val _event: MutableLiveData<Event> = MutableLiveData()

    fun sendAction(action: Action) {
        handleAction(action)
    }

    protected fun setState(state: State) {
        _uiState.value = state
    }

    fun subscribeState(owner: LifecycleOwner, observer: Observer<State>) {
        _uiState.observe(owner, observer)
    }

    protected fun sendEvent(event: Event) {
        _event.value = event
    }

    fun subscribeEvent(owner: LifecycleOwner, observer: Observer<Event>) {
        _event.observe(owner, observer)
    }

    protected abstract fun handleAction(action: Action)
}