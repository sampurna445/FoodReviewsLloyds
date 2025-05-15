package com.lloyds.test.data.remote

import com.lloyds.test.data.remote.dto.FoodReviewDto
import com.lloyds.test.data.remote.dto.ReportDto
import com.lloyds.test.data.remote.dto.ReportResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodReviewApi {
    @GET("api/v1/reports")
    suspend fun getReviews(
        @Query("category") category: String? = null,
        @Query("min_rating") minRating: Int? = null,
        @Query("max_rating") maxRating: Int? = null
    ): FoodReviewDto

    @GET("api/v1/reports/{id}")
    suspend fun getReviewById(
        @Path("id") id: String
    ): ReportResponseDto
} 