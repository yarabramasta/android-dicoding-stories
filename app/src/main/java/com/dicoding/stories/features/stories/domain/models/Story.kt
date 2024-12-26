package com.dicoding.stories.features.stories.domain.models

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Parcelize
@Serializable
data class Story(
  val id: String,
  val name: String,
  val description: String,
  val photoUrl: String,
  val createdAt: String,
  val lat: Double? = null,
  val lon: Double? = null,
) : Parcelable {

  companion object {
    @OptIn(ExperimentalUuidApi::class)
    fun dummy() = Story(
      id = Uuid.random().toString(),
      name = "Dummy Story",
      description = "This is a dummy story...",
      photoUrl = "https://placehold.co/1080.png",
      createdAt = "2021-07-01T00:00:00Z",
    )

    fun placeholder() = Story(
      id = "...",
      name = "...",
      description = "Please wait while we fetching the story details.",
      photoUrl = "",
      createdAt = "..."
    )

  }

  fun getLatLng(): LatLng? {
    return if (lat != null && lon != null) {
      LatLng(lat, lon)
    } else {
      null
    }
  }

  fun getFormattedDate(): String {
    return Instant
      .parse(createdAt)
      .format(
        DateTimeComponents.Format {
          date(
            LocalDate.Format {
              monthName(MonthNames.ENGLISH_ABBREVIATED)
              char(' ')
              dayOfMonth()
              chars(", ")
              year()
            }
          )
        }
      )
  }
}
