package com.chirag047.rapiddeliver.Viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chirag047.rapiddeliver.Common.ResponseType
import com.chirag047.rapiddeliver.Model.Coordinates
import com.chirag047.rapiddeliver.Repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackNowScreenViewModel @Inject constructor(val dataRepository: DataRepository) :

    ViewModel() {

    private val _liveData = MutableStateFlow<ResponseType<Coordinates?>>(ResponseType.Loading())
    val liveData: StateFlow<ResponseType<Coordinates?>>
        get() = _liveData


    private val _doneData = MutableStateFlow<ResponseType<String>>(ResponseType.Loading())
    val doneData: StateFlow<ResponseType<String>>
        get() = _doneData

    suspend fun trackLiveLocation(orderId: String) {
        viewModelScope.launch {
            dataRepository.trackLiveLocation(orderId).collect {
                _liveData.emit(it)
            }
        }
    }

    suspend fun doneMechanicService(
        orderId: String, centerId: String,
        userId: String,
        owner: String
    ) {
        viewModelScope.launch {
            dataRepository.doneMechanicService(orderId, centerId, userId, owner).collect {
                _doneData.emit(it)
            }
        }
    }

}