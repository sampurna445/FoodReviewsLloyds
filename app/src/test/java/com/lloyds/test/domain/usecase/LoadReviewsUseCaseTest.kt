package com.lloyds.test.domain.usecase

import com.lloyds.test.domain.model.Review
import com.lloyds.test.domain.repository.FoodReviewRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LoadReviewsUseCaseTest {

    // Mocked repository
    private lateinit var repository: FoodReviewRepository

    private lateinit var loadReviewsUseCase: LoadReviewsUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        loadReviewsUseCase = LoadReviewsUseCase(repository)
    }

    @Test
    fun `invoke with no args calls repository with nulls and returns its flow`() = runTest {
        // Sample data (empty list here; replace Review with your real model)
        val expected: Flow<List<Review>> = flowOf(emptyList())

        // Stub repository
        coEvery { repository.getReviews(null, null, null) } returns expected

        // Execute
        val result = loadReviewsUseCase()

        // Verify behaviour
        assertEquals(expected, result)
        coVerify(exactly = 1) { repository.getReviews(null, null, null) }
    }

    @Test
    fun `invoke with all params calls repository with provided values`() = runTest {
        val category = "Dessert"
        val minRating = 3
        val maxRating = 5

        val expected: Flow<List<Review>> = flowOf(emptyList())

        coEvery { repository.getReviews(category, minRating, maxRating) } returns expected

        val result = loadReviewsUseCase(category, minRating, maxRating)

        assertEquals(expected, result)
        coVerify(exactly = 1) { repository.getReviews(category, minRating, maxRating) }
    }
}