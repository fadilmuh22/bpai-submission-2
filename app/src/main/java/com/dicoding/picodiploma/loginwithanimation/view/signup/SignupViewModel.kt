package com.dicoding.picodiploma.loginwithanimation.view.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.repository.StoriesRepository
import com.dicoding.picodiploma.loginwithanimation.data.repository.UserRepository
import kotlinx.coroutines.launch

class SignupViewModel(
    private val storiesRepository: StoriesRepository
) :
    ViewModel() {
    fun register(name: String, email: String, password: String) =
        storiesRepository.register(name, email, password)

}
