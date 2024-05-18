package com.dicoding.picodiploma.loginwithanimation.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.picodiploma.loginwithanimation.data.Result
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ErrorResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.StoriesResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.ApiService
import com.google.gson.Gson
import retrofit2.HttpException

class StoriesRepository private constructor(
    private val apiService: ApiService,
) {
    fun register(
        name: String,
        email: String,
        password: String,
    ): LiveData<Result<RegisterResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.register(name, email, password)
                Log.d(TAG, response.toString())
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                emit(Result.Error(errorBody.message ?: "Server error"))
            } catch (e: Exception) {
                Log.d(TAG, "register: ${e.message}")
                emit(Result.Error(e.message.toString()))
            }
        }

    fun login(
        email: String,
        password: String,
    ): LiveData<Result<RegisterResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.login(email, password)
                Log.d(TAG, response.toString())
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                emit(Result.Error(errorBody.message ?: "Server error"))
            } catch (e: Exception) {
                Log.d(TAG, "login: ${e.message}")
                emit(Result.Error(e.message.toString()))
            }
        }

    fun getStories(): LiveData<Result<StoriesResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getStories()
                Log.d(TAG, response.toString())
                emit(Result.Success(response))
            } catch (e: Exception) {
                Log.d(TAG, "login: ${e.message}")
                emit(Result.Error(e.message.toString()))
            }
        }

    companion object {
        const val TAG = "StoriesRepository"

        @Volatile
        private var instance: StoriesRepository? = null

        fun getInstance(apiService: ApiService): StoriesRepository =
            instance ?: synchronized(this) {
                instance ?: StoriesRepository(apiService)
            }.also { instance = it }
    }
}
