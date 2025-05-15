package com.lloyds.test.data.remote.dto

import androidx.annotation.Keep

@Keep
data class FoodReviewDto(
    val reports: List<ReportDto>,
)
