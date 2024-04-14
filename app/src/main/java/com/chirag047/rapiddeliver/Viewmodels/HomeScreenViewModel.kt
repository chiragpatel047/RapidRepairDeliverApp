package com.chirag047.rapiddeliver.Viewmodels

import androidx.lifecycle.ViewModel
import com.chirag047.rapiddeliver.Repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel() {

    suspend fun getUserDetail() = dataRepository.getUserDetail()
}