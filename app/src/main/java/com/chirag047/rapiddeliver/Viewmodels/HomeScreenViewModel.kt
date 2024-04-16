package com.chirag047.rapiddeliver.Viewmodels

import androidx.lifecycle.ViewModel
import com.chirag047.rapiddeliver.Repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel() {

    suspend fun getUserDetail() = dataRepository.getUserDetail()
    suspend fun getPendingRequest(mechanicId: String) = dataRepository.getPendingRequest(mechanicId)

    suspend fun startMechanicService(orderId: String) =
        dataRepository.startMechanicService(orderId)

    suspend fun getLiveRequest(mechanicId: String) = dataRepository.getLiveRequest(mechanicId)
}