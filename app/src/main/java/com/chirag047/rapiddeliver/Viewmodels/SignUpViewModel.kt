package com.chirag047.rapiddeliver.Viewmodels

import androidx.lifecycle.ViewModel
import com.chirag047.rapiddeliver.Repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(val authRepository: AuthRepository) : ViewModel() {

    suspend fun createUser(username: String, email: String, password: String) =
        authRepository.createUser(username, email, password)

}