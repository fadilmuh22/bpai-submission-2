package com.dicoding.picodiploma.loginwithanimation.view.addstory

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.repository.StoriesRepository
import java.io.File

class AddStoryViewModel(
    private val storiesRepository: StoriesRepository,
) : ViewModel() {
    fun uploadStory(
        imageStory: File,
        description: String,
    ) = storiesRepository.uploadStory(imageStory, description)
}
