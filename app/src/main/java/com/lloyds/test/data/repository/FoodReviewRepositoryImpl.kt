package com.lloyds.test.data.repository

import com.lloyds.test.data.remote.FoodReviewApi
import com.lloyds.test.data.remote.dto.toReview
import com.lloyds.test.domain.model.Review
import com.lloyds.test.domain.repository.FoodReviewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FoodReviewRepositoryImpl
    @Inject
    constructor(
        private val api: FoodReviewApi,
    ) : FoodReviewRepository {
        override fun getReviews(
            category: String?,
            minRating: Int?,
            maxRating: Int?,
        ): Flow<List<Review>> =
            flow {
                val reviews =
                    api
                        .getReviews(
                            category = category,
                            minRating = minRating,
                            maxRating = maxRating,
                        ).reports
                        .map { it.toReview() }
                emit(reviews)
            }

        override fun getReviewById(id: String): Flow<Review> =
            flow {
                val review = api.getReviewById(id).report.toReview()
                emit(review)
            }
    }
