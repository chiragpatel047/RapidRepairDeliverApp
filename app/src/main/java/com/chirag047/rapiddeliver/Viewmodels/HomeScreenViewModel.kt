package com.chirag047.rapiddeliver.Viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chirag047.rapiddeliver.Common.ResponseType
import com.chirag047.rapiddeliver.Model.OrderModel
import com.chirag047.rapiddeliver.Repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel() {

    private val _liveRequests =
        MutableStateFlow<ResponseType<List<OrderModel>>>(ResponseType.Loading())
    val liveRequests: StateFlow<ResponseType<List<OrderModel>>>
        get() = _liveRequests


    private val _pendingRequests =
        MutableStateFlow<ResponseType<List<OrderModel>>>(ResponseType.Loading())
    val pendingRequests: StateFlow<ResponseType<List<OrderModel>>>
        get() = _pendingRequests


    private val _doneData = MutableStateFlow<ResponseType<String>>(ResponseType.Loading())
    val doneData: StateFlow<ResponseType<String>>
        get() = _doneData


    private val _statusChanged = MutableStateFlow<ResponseType<String>>(ResponseType.Loading())
    val statusChanged: StateFlow<ResponseType<String>>
        get() = _statusChanged


    suspend fun getUserDetail() = dataRepository.getUserDetail()
    suspend fun getPendingRequest(mechanicId: String) {
        viewModelScope.launch {
            dataRepository.getPendingRequest(mechanicId).collect {
                _pendingRequests.emit(it)
            }
        }
    }


    suspend fun doneMechanicService(
        orderId: String,
        centerId: String,
        userId: String,
        owner: String
    ) {
        viewModelScope.launch {
            dataRepository.doneMechanicService(orderId, centerId, userId, owner).collect {
                _doneData.emit(it)
            }
        }
    }

    suspend fun startMechanicService(
        orderId: String,
        centerId: String,
        owner: String,
        userId: String
    ) =
        dataRepository.startMechanicService(orderId, centerId, owner, userId)

    suspend fun getLiveRequest(mechanicId: String) {
        viewModelScope.launch {
            dataRepository.getLiveRequest(mechanicId).collect {
                _liveRequests.emit(it)
            }
        }

    }

    suspend fun updateMechanicStatus(status: String) {
        viewModelScope.launch {
            dataRepository.updateMechanicStatus(status).collect {
                _statusChanged.emit(it)
            }
        }
    }
}