package com.dicoding.picodiploma.loginwithanimation.view.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.Result
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.adapter.StoryAdapter
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)

        viewModel.getSession().observe(this) { user ->
            Log.d("MainActivity", "User: ${user.isLogin}")
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        val adapter = StoryAdapter()
        binding.rvMainStories.adapter = adapter;

        fetchStories(adapter)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_main_logout -> {
            viewModel.logout()
            Log.d("MainActivity", "Logout called.")
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun fetchStories(storyAdapter: StoryAdapter) {
        viewModel.getStories().observe(this) {
            when(it) {
                is Result.Error -> {
                    Log.e("MainActivity", "Error: ${it.error}")
                }
                Result.Loading -> {
                    Log.d("MainActivity", "Loading...")
                }
                is Result.Success -> {
                    storyAdapter.submitList(it.data.listStory)
                }
            }
        }
    }
}