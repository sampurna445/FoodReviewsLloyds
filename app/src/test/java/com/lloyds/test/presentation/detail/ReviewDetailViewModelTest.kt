package com.lloyds.test.presentation.detail

import com.lloyds.test.domain.model.Review
import com.lloyds.test.domain.repository.FoodReviewRepository
import com.lloyds.test.presentation.details.ReviewDetailState
import com.lloyds.test.presentation.details.ReviewDetailViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ReviewDetailViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setupDispatcher() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is default`() = runTest {
        val repo = mockk<FoodReviewRepository>()
        val vm = ReviewDetailViewModel(repo)

        val state = vm.state.value
        assertNull("Review should be null", state.review)
        assertFalse("isLoading should be false", state.isLoading)
        assertNull("error should be null", state.error)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getReview emits loading then success`() = runTest {
        // Given
        val sampleReport = Review(
            id = "1",
            product = "Test Product",
            manufacturer = "Test Manufacturer",
            category = "Category",
            rating = 5.5,
            dateReleased = "2025-05-01",
            videoCode = "code",
            videoTitle = "title"
        )
        val repo = mockk<FoodReviewRepository>()
        coEvery { repo.getReviewById("1") } returns flowOf(sampleReport)

        // When
        val vm = ReviewDetailViewModel(repo)
        val states = mutableListOf<ReviewDetailState>()
        val job = launch(testDispatcher) { vm.state.toList(states) }

        vm.getReview("1")
        advanceUntilIdle()
        job.cancel()

        // Then
        val finalState = states.last()
        assertEquals("Expected report in state", sampleReport, finalState.review)
        assertFalse("Loading should be false", finalState.isLoading)
        assertNull("Error should be null", finalState.error)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getReview emits error when exception`() = runTest {
        // Given
        val repo = mockk<FoodReviewRepository>()
        val exception = RuntimeException("Network failure")
        coEvery { repo.getReviewById(any()) } throws exception

        // When
        val vm = ReviewDetailViewModel(repo)
        vm.getReview("anyId")
        advanceUntilIdle()

        // Then
        val state = vm.state.value
        assertEquals("Error message should match", "Network failure", state.error)
        assertFalse("Loading should be false on error", state.isLoading)
    }
}