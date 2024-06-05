package com.dicoding.picodiploma.loginwithanimation.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.picodiploma.loginwithanimation.data.ResultState
import com.dicoding.picodiploma.loginwithanimation.data.StoriesRemoteMediator
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ErrorMessageResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.StoriesResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.ApiService
import com.dicoding.picodiploma.loginwithanimation.data.room.StoriesDatabase
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoriesRepository private constructor(
    private val storiesDatabase: StoriesDatabase,
    private val apiService: ApiService,
) {
    fun register(
        name: String,
        email: String,
        password: String,
    ): LiveData<ResultState<ErrorMessageResponse>> =
        liveData {
            emit(ResultState.Loading)
            try {
                val response = apiService.register(name, email, password)
                Log.d(TAG, response.toString())
                emit(ResultState.Success(response))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorMessageResponse::class.java)
                emit(ResultState.Error(errorBody.message ?: "Server error"))
            } catch (e: Exception) {
                Log.d(TAG, "register: ${e.message}")
                emit(ResultState.Error(e.message.toString()))
            }
        }

    fun login(
        email: String,
        password: String,
    ): LiveData<ResultState<LoginResponse>> =
        liveData {
            emit(ResultState.Loading)
            try {
                val response = apiService.login(email, password)
                Log.d(TAG, response.toString())
                emit(ResultState.Success(response))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorMessageResponse::class.java)
                emit(ResultState.Error(errorBody.message ?: "Server error"))
            } catch (e: Exception) {
                Log.d(TAG, "login: ${e.message}")
                emit(ResultState.Error(e.message.toString()))
            }
        }

    fun getStoriesWithLocation(): LiveData<ResultState<StoriesResponse>> =
        liveData {
            emit(ResultState.Loading)
            try {
                val response = apiService.getStories(null, null, 1)
                Log.d(TAG, response.toString())
                emit(ResultState.Success(response))
            } catch (e: Exception) {
                Log.d(TAG, "login: ${e.message}")
                emit(ResultState.Error(e.message.toString()))
            }
        }

    fun getStoriesPaging(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config =
                PagingConfig(
                    pageSize = 5,
                ),
            remoteMediator = StoriesRemoteMediator(storiesDatabase, apiService),
            pagingSourceFactory = {
                storiesDatabase.storyDao().getAllStories()
            },
        ).liveData
    }

    fun uploadStory(
        imageStory: File,
        description: String,
    ) = liveData {
        emit(ResultState.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageStory.asRequestBody("image/jpeg".toMediaType())
        val multipartBody =
            MultipartBody.Part.createFormData(
                "photo",
                imageStory.name,
                requestImageFile,
            )
        try {
            val successResponse = apiService.uploadStory(multipartBody, requestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorMessageResponse::class.java)
            emit(ResultState.Error(errorResponse.message ?: "Server error"))
        }
    }

    companion object {
        const val TAG = "StoriesRepository"

        @Volatile
        private var instance: StoriesRepository? = null

        fun getInstance(
            storiesDatabase: StoriesDatabase,
            apiService: ApiService,
        ): StoriesRepository =
            instance ?: synchronized(this) {
                instance ?: StoriesRepository(storiesDatabase, apiService)
            }.also { instance = it }

        @JvmStatic
        fun clearInstance() {
            instance = null
        }
    }
}
