package com.chirag047.rapiddeliver.Viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chirag047.rapiddeliver.Common.ResponseType
import com.chirag047.rapiddeliver.Model.OrderModel
import com.chirag047.rapiddeliver.Repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryScreenViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel() {

    private val _historyRequests =
        MutableStateFlow<ResponseType<List<OrderModel>>>(ResponseType.Loading())
    val historyRequests: StateFlow<ResponseType<List<OrderModel>>>
        get() = _historyRequests

    suspend fun getMyOrdersRequest(mechanicId: String) {
        viewModelScope.launch {
            dataRepository.getMyOrdersRequest(mechanicId).collect {
                _historyRequests.emit(it)
            }
        }
    }

}