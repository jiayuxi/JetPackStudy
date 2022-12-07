package com.jiayx.component.mvi.viewmodel

import android.os.health.ServiceHealthStats
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiayx.component.mvi.model.LoginAction
import com.jiayx.component.mvi.model.LoginViewEvent
import com.jiayx.component.mvi.model.LoginViewState
import com.jiayx.component.mvi.utils.SharedFlowEvents
import com.jiayx.component.mvi.utils.setEvent
import com.jiayx.component.mvi.utils.setState
import com.jiayx.component.mvi.utils.withState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 *  author : Jia yu xi
 *  date : 2022/12/7 21:47:47
 *  description :
 */
class MainViewModel : ViewModel() {
    private val _viewState = MutableStateFlow(LoginViewState())
    val viewState = _viewState.asStateFlow()

    private val _viewEvent = SharedFlowEvents<LoginViewEvent>()
    val viewEvent = _viewEvent.asSharedFlow()

    val channel = Channel<LoginAction>()

    init {
        handlerAction()
    }

    private fun handlerAction() {
        viewModelScope.launch {
            channel.consumeAsFlow().collect {
                when (it) {
                    is LoginAction.UpdateUserName -> {
                        updateUserName(it.userName)
                    }
                    is LoginAction.UpdatePassword -> {
                        updatePassword(it.password)
                    }
                    is LoginAction.Login -> {
                        login()
                    }
                }
            }
        }
    }

    private fun updateUserName(userName: String) {
        _viewState.setState {
            copy(userName = userName)
        }
    }

    private fun updatePassword(password: String) {
        _viewState.setState {
            copy(password = password)
        }
    }

    private fun login() {
        viewModelScope.launch {
            flow {
                loginLogic()
                emit("登录成功")
            }.onStart {
                _viewEvent.setEvent(LoginViewEvent.ShowLoadingDialog)
            }.onEach {
                _viewEvent.setEvent(
                    LoginViewEvent.DismissLoadingDialog, LoginViewEvent.ShowToast(it)
                )
            }.catch {
                _viewState.setState { copy(password = "") }
                _viewEvent.setEvent(
                    LoginViewEvent.DismissLoadingDialog, LoginViewEvent.ShowToast("登录失败")
                )
            }.collect()
        }
    }

    private suspend fun loginLogic() {
        withState(viewState) {
            val userName = it.userName
            val password = it.password
            delay(2000)
            throw Exception("登录失败")
            "$userName,$password"
        }
    }
}