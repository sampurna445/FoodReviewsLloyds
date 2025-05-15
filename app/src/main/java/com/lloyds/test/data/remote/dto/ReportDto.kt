package com.lloyds.test.data.remote.dto

import androidx.annotation.Keep
import com.lloyds.test.domain.model.Review

@Keep
data class ReportDto(
    val category: String?,
    val dateReleased: String,
    val id: String,
    val manufacturer: String,
    val product: String,
    val rating: Double,
    val videoCode: String,
    val videoTitle: String,
)

fun ReportDto.toReview(): Review =
    Review(
        id = id,
        product = product,
        manufacturer = manufacturer,
        category = category ?: "Others",
        rating = rating,
        dateReleased = dateReleased,
        videoCode = videoCode,
        videoTitle = videoTitle,
    )
