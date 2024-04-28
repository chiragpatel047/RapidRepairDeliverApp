package com.chirag047.rapiddeliver.Viewmodels


import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chirag047.rapiddeliver.Repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel() {

    suspend fun updateUserProfilePictureAndPhone(
        userImage: String,
        userName: String,
        phoneNo: String,
        sharedPreferences: SharedPreferences
    ) =
        dataRepository.updateUserProfilePictureAndPhone(
            userImage,
            userName,
            phoneNo,
            sharedPreferences
        )

    suspend fun getUserDetails() = dataRepository.getUserDetail()
}