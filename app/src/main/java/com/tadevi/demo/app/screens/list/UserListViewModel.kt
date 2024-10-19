package com.tadevi.demo.app.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tadevi.demo.domain.entity.UserBrief
import com.tadevi.demo.domain.usecase.GetUserPagingDataUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserListViewModel(
    private val getUserPagingDataUseCase: GetUserPagingDataUseCase
) : ViewModel() {
    private val _stateFlow = MutableStateFlow<UserListUIState>(UserListUIState.Initial)

    val stateFlow = _stateFlow.asStateFlow()

    fun loadData() {
        viewModelScope.launch {
            val state = getUserPagingDataUseCase.execute(Unit).fold(
                onSuccess = { flow -> UserListUIState.Success(flow.cachedIn(viewModelScope)) },
                onFailure = { UserListUIState.Fail }
            )
            _stateFlow.update { state }
        }
    }
}

sealed interface UserListUIState {
    data object Initial : UserListUIState // to prevent nullable state in stateflow

    data object Fail : UserListUIState // indicate an error happened

    data class Success(val pagingFlow: Flow<PagingData<UserBrief>>) : UserListUIState // paging data
}