package com.dicoding.picodiploma.loginwithanimation

import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem

object DataDummy {
    fun generateDummyStories(): List<ListStoryItem> {
        val newsList = ArrayList<ListStoryItem>()
        for (i in 0..10) {
            val news =
                ListStoryItem(
                    id = "id_$i",
                    name = "name_$i",
                    description = "description_$i",
                    photoUrl = "photoUrl_$i",
                    createdAt = "createdAt_$i",
                    lat = i.toDouble(),
                    lon = i.toDouble(),
                )
            newsList.add(news)
        }
        return newsList
    }
}
