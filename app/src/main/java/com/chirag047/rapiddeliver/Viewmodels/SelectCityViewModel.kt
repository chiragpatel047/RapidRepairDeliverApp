package com.chirag047.rapiddeliver.Viewmodels

import androidx.lifecycle.ViewModel
import com.chirag047.rapiddeliver.Repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectCityViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel() {

    suspend fun updateUserCity(city: String,mechanicId : String) = dataRepository.updateUserCity(city,mechanicId)

}