package com.fahimshahrierrasel.syncacross.viewmodels

import android.util.Patterns
import com.fahimshahrierrasel.syncacross.config.FirebaseConfig
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class LoginViewState(val isLoading: Boolean = true, val message: String = "")

sealed class LoginShotEvent {
    object NavigateToHome : LoginShotEvent()
    object LoginError : LoginShotEvent()
}

enum class LoginNavigate {
    Stay,
    GoHome
}

sealed class LoginUIAction {
    class LoginUser(val email: String, val password: String) : LoginUIAction()
    class Navigate(val navigate: LoginNavigate) : LoginUIAction()
}

class LoginViewModel {
    private val coroutineScope = MainScope()
    private val _viewState: MutableStateFlow<LoginViewState> = MutableStateFlow(LoginViewState())
    private val _shotEvents = Channel<LoginShotEvent>(Channel.BUFFERED)

    val viewState = _viewState.asStateFlow()
    val shotEvents = _shotEvents.receiveAsFlow()

    fun onAction(uiAction: LoginUIAction) {
        when (uiAction) {
            is LoginUIAction.LoginUser -> {
                coroutineScope.launch {
                    try {
                        if (!validateCredential(uiAction.email, uiAction.password)) {
                            return@launch
                        }
                        setLoadingState(true)
                        FirebaseConfig.auth.signInWithEmailAndPassword(
                            uiAction.email,
                            uiAction.password
                        ).await()
                        _shotEvents.send(LoginShotEvent.NavigateToHome)
                    } catch (e: Exception) {
                        setMessage("Login failed, Error: ${e.localizedMessage}")
                    } finally {
                        setLoadingState(false)
                    }
                }
            }
            is LoginUIAction.Navigate -> {
                coroutineScope.launch {
                    when (uiAction.navigate) {
                        LoginNavigate.Stay -> {
                            setLoadingState(false)
                        }
                        LoginNavigate.GoHome -> {
                            _shotEvents.send(LoginShotEvent.NavigateToHome)
                            setLoadingState(false)
                        }
                    }
                }
            }
        }
    }

    private suspend fun validateCredential(email: String, password: String): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setMessage("Email address is not correct!")
            return false
        } else if (password.length < 8) {
            setMessage("Password is shorter than 8 characters!")
            return false
        }
        return true
    }

    private fun setLoadingState(loadingState: Boolean) {
        _viewState.value = _viewState.value.copy(isLoading = loadingState)
    }

    private suspend fun setMessage(message: String) {
        _viewState.value = _viewState.value.copy(message = message)
        _shotEvents.send(LoginShotEvent.LoginError)
    }
}