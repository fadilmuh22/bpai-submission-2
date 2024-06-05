package com.dicoding.picodiploma.loginwithanimation.view.storymaps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.ResultState
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.StoriesResponse
import com.dicoding.picodiploma.loginwithanimation.data.repository.StoriesRepository

class StoryMapsViewModel(
    private val storiesRepository: StoriesRepository,
) : ViewModel() {
    fun getStoriesWithLocation(): LiveData<ResultState<StoriesResponse>> {
        return storiesRepository.getStoriesWithLocation()
    }
}
