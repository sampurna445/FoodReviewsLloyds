package com.lloyds.test.presentation.reviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lloyds.test.domain.model.Review
import com.lloyds.test.domain.usecase.LoadReviewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReviewsState(
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedCategory: String? = null,
    val minRating: Int? = null,
    val maxRating: Int? = null,
    val categories: List<String> = emptyList(),
    val sortOrder: SortOrder = SortOrder.NONE,
)

enum class SortOrder {
    ASCENDING,
    DESCENDING,
    NONE,
}

sealed class ReviewsIntent {
    data class LoadReviews(
        val category: String? = null,
        val minRating: Int? = null,
        val maxRating: Int? = null,
    ) : ReviewsIntent()

    data class UpdateCategory(
        val category: String?,
    ) : ReviewsIntent()

    data class UpdateMinRating(
        val rating: Int?,
    ) : ReviewsIntent()

    data class UpdateMaxRating(
        val rating: Int?,
    ) : ReviewsIntent()
}

@HiltViewModel
class ReviewsViewModel
    @Inject
    constructor(
        private val loadReviewsUseCase: LoadReviewsUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow(ReviewsState())
        val state: StateFlow<ReviewsState> = _state.asStateFlow()

        init {
            loadReviews()
        }

        fun onIntent(intent: ReviewsIntent) {
            when (intent) {
                is ReviewsIntent.LoadReviews ->
                    loadReviews(
                        category = intent.category,
                        minRating = intent.minRating,
                        maxRating = intent.maxRating,
                    )

                is ReviewsIntent.UpdateCategory -> {
                    _state.update { it.copy(selectedCategory = intent.category) }
                    loadReviews(
                        category = intent.category,
                        minRating = state.value.minRating,
                        maxRating = state.value.maxRating,
                    )
                }

                is ReviewsIntent.UpdateMinRating -> {
                    _state.update {
                        it.copy(
                            minRating = intent.rating,
                            maxRating = null,
                            sortOrder = if (intent.rating != null) SortOrder.ASCENDING else SortOrder.NONE,
                        )
                    }
                    loadReviews(
                        category = state.value.selectedCategory,
                        minRating = intent.rating,
                        maxRating = null,
                    )
                }

                is ReviewsIntent.UpdateMaxRating -> {
                    _state.update {
                        it.copy(
                            maxRating = intent.rating,
                            minRating = null,
                            sortOrder = if (intent.rating != null) SortOrder.DESCENDING else SortOrder.NONE,
                        )
                    }
                    loadReviews(
                        category = state.value.selectedCategory,
                        minRating = null,
                        maxRating = intent.rating,
                    )
                }
            }
        }

        private fun loadReviews(
            category: String? = null,
            minRating: Int? = null,
            maxRating: Int? = null,
        ) {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true, error = null) }
                try {
                    loadReviewsUseCase(
                        category = category,
                        minRating = minRating,
                        maxRating = maxRating,
                    ).collect { reviews ->
                        val sortedReviews =
                            when (state.value.sortOrder) {
                                SortOrder.ASCENDING -> reviews.sortedBy { it.rating }
                                SortOrder.DESCENDING -> reviews.sortedByDescending { it.rating }
                                SortOrder.NONE -> reviews
                            }
                        _state.update {
                            it.copy(
                                reviews = sortedReviews,
                                isLoading = false,
                            )
                        }
                    }
                } catch (e: Exception) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "An error occurred",
                        )
                    }
                }
            }
        }
    }
