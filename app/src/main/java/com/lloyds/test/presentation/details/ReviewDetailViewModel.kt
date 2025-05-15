package com.lloyds.test.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lloyds.test.domain.model.Review
import com.lloyds.test.domain.repository.FoodReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReviewDetailState(
    val review: Review? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class ReviewDetailViewModel
    @Inject
    constructor(
        private val repository: FoodReviewRepository,
    ) : ViewModel() {
        private val _state = MutableStateFlow(ReviewDetailState())
        val state: StateFlow<ReviewDetailState> = _state.asStateFlow()

        fun getReview(reviewId: String) {
            viewModelScope.launch {
                try {
                    _state.update { it.copy(isLoading = true, error = null) }
                    repository.getReviewById(reviewId).collect { review ->
                        _state.update {
                            it.copy(
                                review = review,
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
