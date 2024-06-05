package com.dicoding.picodiploma.loginwithanimation.view.storymaps

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.ResultState
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityStoryMapsBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class StoryMapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val viewModel by viewModels<StoryMapsViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityStoryMapsBinding
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoryMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment =
            supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        setMapStyle()
        fetchStories()
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Toast.makeText(this, getString(R.string.style_parsing_failed), Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (exception: Resources.NotFoundException) {
            Toast.makeText(this, getString(R.string.can_t_find_style_error), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun fetchStories() {
        viewModel.getStoriesWithLocation().observe(this) {
            when (it) {
                is ResultState.Error -> {
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                }

                ResultState.Loading -> {
                    Log.d("StoryMapsActivity", "Loading")
                }

                is ResultState.Success -> {
                    if (it.data.listStory.isEmpty()) {
                        Toast.makeText(
                            this,
                            getString(R.string.can_t_find_any_story_with_location),
                            Toast.LENGTH_SHORT,
                        ).show()
                    } else {
                        addManyMarker(it.data.listStory)
                    }
                }
            }
        }
    }

    private fun addManyMarker(listStoryItem: List<ListStoryItem>) {
        listStoryItem.forEach { storyItem ->
            if (storyItem.lat != null && storyItem.lon != null && storyItem.lat != 0.0 && storyItem.lon != 0.0) {
                val latLng = LatLng(storyItem.lat, storyItem.lon)
                mMap.addMarker(MarkerOptions().position(latLng).title(storyItem.name))
                boundsBuilder.include(latLng)
            }
        }

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300,
            ),
        )
    }
}
