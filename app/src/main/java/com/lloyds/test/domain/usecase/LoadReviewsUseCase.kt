package com.lloyds.test.domain.usecase

import com.lloyds.test.domain.repository.FoodReviewRepository
import javax.inject.Inject

class LoadReviewsUseCase @Inject constructor(
    val repository: FoodReviewRepository,
) {
    operator fun invoke(
        category: String? = null,
        minRating: Int? = null,
        maxRating: Int? = null
    ) = repository.getReviews(
        category = category,
        minRating = minRating,
        maxRating = maxRating
    )
}