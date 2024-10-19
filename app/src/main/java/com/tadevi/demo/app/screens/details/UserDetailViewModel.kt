package com.tadevi.demo.app.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tadevi.demo.domain.entity.UserDetail
import com.tadevi.demo.domain.usecase.GetUserDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserDetailViewModel(
    private val getUserDetailUseCase: GetUserDetailUseCase
): ViewModel() {
    private val _state = MutableStateFlow<UserDetailUIState>(UserDetailUIState.Loading)

    val state = _state.asStateFlow()

    fun loadData(username: String) {
        viewModelScope.launch {
            _state.update { UserDetailUIState.Loading }

            getUserDetailUseCase.execute(username).fold(
                onSuccess = { detail ->
                    _state.update { UserDetailUIState.Success(detail) }
                },
                onFailure = {
                    _state.update { UserDetailUIState.Fail }
                },
            )
        }
    }
}

sealed interface UserDetailUIState {
    data object Loading: UserDetailUIState
    data object Fail: UserDetailUIState
    data class Success(val data: UserDetail): UserDetailUIState
}