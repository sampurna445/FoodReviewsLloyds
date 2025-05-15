package com.lloyds.test.domain.repository

import com.lloyds.test.domain.model.Review
import kotlinx.coroutines.flow.Flow

interface FoodReviewRepository {
    fun getReviews(
        category: String? = null,
        minRating: Int? = null,
        maxRating: Int? = null
    ): Flow<List<Review>>
    
    fun getReviewById(id: String): Flow<Review>
} 