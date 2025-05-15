package com.lloyds.test.presentation.reviews

import com.lloyds.test.domain.model.Review
import com.lloyds.test.domain.usecase.LoadReviewsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isNull

@OptIn(ExperimentalCoroutinesApi::class)
class ReviewsViewModelTest {
    private lateinit var viewModel: ReviewsViewModel
    private lateinit var loadReviewsUseCase: LoadReviewsUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        loadReviewsUseCase = mockk()
        coEvery { loadReviewsUseCase() } returns flow { emit(emptyList()) }
        viewModel = ReviewsViewModel(loadReviewsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be correct`() =
        runTest {
            // When
            val state = viewModel.state.first()

            // Then
            expectThat(state) {
                get { isLoading }.isFalse()
                get { error }.isNull()
                get { reviews }.isEmpty()
            }
        }

    @Test
    fun `when loading reviews, state should show loading`() =
        runTest {
            // Given
            val reviews =
                listOf(
                    Review(
                        id = "1",
                        product = "Test Product",
                        manufacturer = "Test Manufacturer",
                        category = "Test Category",
                        rating = 8.5,
                        dateReleased = "2024-03-20",
                        videoCode = "test123",
                        videoTitle = "Test Video",
                    ),
                )
            coEvery { loadReviewsUseCase() } returns flow { emit(reviews) }

            // When
            viewModel.onIntent(ReviewsIntent.LoadReviews())
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val state = viewModel.state.first()
            expectThat(state) {
                get { isLoading }.isFalse()
                get { error }.isNull()
                get { reviews }.isEqualTo(reviews)
            }
        }

    @Test
    fun `when loading reviews fails, state should show error`() =
        runTest {
            // Given
            val errorMessage = "Network error"
            coEvery { loadReviewsUseCase() } throws Exception(errorMessage)

            // When
            viewModel.onIntent(ReviewsIntent.LoadReviews())
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val state = viewModel.state.first()
            expectThat(state) {
                get { isLoading }.isFalse()
                get { error }.isEqualTo(errorMessage)
                get { reviews }.isEmpty()
            }
        }

    @Test
    fun `when updating category, state selectedCategory and reviews update`() =
        runTest {
            // Given
            val category = "Dessert"
            val reviews =
                listOf(
                    Review(
                        id = "1",
                        product = "P",
                        manufacturer = "M",
                        category = category,
                        rating = 4.0,
                        dateReleased = "2024-01-01",
                        videoCode = "c",
                        videoTitle = "t",
                    ),
                )
            coEvery { loadReviewsUseCase(category, null, null) } returns flow { emit(reviews) }

            // When
            viewModel.onIntent(ReviewsIntent.UpdateCategory(category))
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val state = viewModel.state.first()
            expectThat(state) {
                get { selectedCategory }.isEqualTo(category)
                get { reviews }.isEqualTo(reviews)
                get { isLoading }.isFalse()
                get { error }.isNull()
            }
            coVerify { loadReviewsUseCase(category, null, null) }
        }

    @Test
    fun `when updating min rating, state minRating, sortOrder and reviews update`() =
        runTest {
            // Given
            val minRating = 3
            val reviews =
                listOf(
                    Review(
                        id = "2",
                        product = "X",
                        manufacturer = "Y",
                        category = "Z",
                        rating = minRating.toDouble(),
                        dateReleased = "2024-02-02",
                        videoCode = "c2",
                        videoTitle = "t2",
                    ),
                )
            coEvery { loadReviewsUseCase(null, minRating, null) } returns flow { emit(reviews) }

            // When
            viewModel.onIntent(ReviewsIntent.UpdateMinRating(minRating))
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val state = viewModel.state.first()
            expectThat(state) {
                get { minRating }.isEqualTo(minRating)
                get { maxRating }.isNull()
                get { sortOrder }.isEqualTo(SortOrder.ASCENDING)
                get { reviews }.isEqualTo(reviews)
            }
            coVerify { loadReviewsUseCase(null, minRating, null) }
        }

    @Test
    fun `when updating max rating, state maxRating, sortOrder and reviews update`() =
        runTest {
            // Given
            val maxRating = 5
            val reviews =
                listOf(
                    Review(
                        id = "3",
                        product = "A",
                        manufacturer = "B",
                        category = "Others",
                        rating = maxRating.toDouble(),
                        dateReleased = "2024-03-03",
                        videoCode = "c3",
                        videoTitle = "t3",
                    ),
                )
            coEvery { loadReviewsUseCase(null, null, maxRating) } returns flow { emit(reviews) }

            // When
            viewModel.onIntent(ReviewsIntent.UpdateMaxRating(maxRating))
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val state = viewModel.state.first()
            expectThat(state) {
                get { maxRating }.isEqualTo(maxRating)
                get { minRating }.isNull()
                get { sortOrder }.isEqualTo(SortOrder.DESCENDING)
                get { reviews }.isEqualTo(reviews)
            }
            coVerify { loadReviewsUseCase(null, null, maxRating) }
        }
}
