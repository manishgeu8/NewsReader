package com.leo.newsreaderapp.base

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val viewStateInternalLiveData = mutableStateOf(ViewModelState(isLoading = false))
    val viewStateLiveData: State<ViewModelState>
        get() = viewStateInternalLiveData

    protected open fun loading() {
        publishViewState(ViewModelState(isLoading = true))
    }

    protected open fun loaded() {
        publishViewState(ViewModelState(isLoading = false, state = ViewState.Success.state))
    }

    protected open fun error(errorMessage: String? = null) {
        publishViewState(ViewModelState(isLoading = false, state = ViewState.Error.state, message = errorMessage))
    }

    private fun publishViewState(viewModelState: ViewModelState) {
        viewModelScope.launch(Dispatchers.Main) {
            viewStateInternalLiveData.value = viewModelState
        }
    }

}

sealed class ViewState(
    val state: String
) {
    object Success : ViewState("success")
    object Error : ViewState("error")
}

data class ViewModelState(
    val state: String? = null,
    val title: String? = null,
    val message: String? = null,
    val isLoading: Boolean = false
)