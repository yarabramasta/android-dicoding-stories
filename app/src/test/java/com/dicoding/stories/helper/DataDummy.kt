package com.dicoding.stories.helper

import com.dicoding.stories.features.stories.domain.models.Story

object DataDummy {
  fun generateDummyStoryResponse(): List<Story> {
    val items: MutableList<Story> = arrayListOf()
    for (i in 0..100) {
      items.add(Story.dummy())
    }
    return items
  }
}
