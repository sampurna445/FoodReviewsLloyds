package com.lloyds.test.data.repository

import com.lloyds.test.data.remote.FoodReviewApi
import com.lloyds.test.data.remote.dto.FoodReviewDto
import com.lloyds.test.data.remote.dto.ReportDto
import com.lloyds.test.data.remote.dto.ReportResponseDto
import com.lloyds.test.data.remote.dto.toReview
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import kotlin.test.assertFailsWith

class FoodReviewRepositoryImplTest {
    private lateinit var repository: FoodReviewRepositoryImpl
    private lateinit var api: FoodReviewApi

    @Before
    fun setup() {
        api = mockk()
        repository = FoodReviewRepositoryImpl(api)
    }

    @Test
    fun `getReviews should return mapped reviews from API`() =
        runTest {
            // Given
            val reportDto =
                ReportDto(
                    category = "Test Category",
                    dateReleased = "2024-03-20",
                    id = "1",
                    manufacturer = "Test Manufacturer",
                    product = "Test Product",
                    rating = 8.5,
                    videoCode = "test123",
                    videoTitle = "Test Video",
                )
            coEvery { api.getReviews() } returns FoodReviewDto(listOf(reportDto))

            // When
            val result = repository.getReviews().first()

            // Then
            expectThat(result) {
                get { size }.isEqualTo(1)
                get { first() }.isEqualTo(reportDto.toReview())
            }
        }

    @Test
    fun `getReviewById should return mapped review from API`() =
        runTest {
            // Given
            val reportDto =
                ReportDto(
                    category = "Test Category",
                    dateReleased = "2024-03-20",
                    id = "1",
                    manufacturer = "Test Manufacturer",
                    product = "Test Product",
                    rating = 8.5,
                    videoCode = "test123",
                    videoTitle = "Test Video",
                )
            coEvery { api.getReviewById("1") } returns ReportResponseDto(reportDto)

            // When
            val result = repository.getReviewById("1").first()

            // Then
            expectThat(result).isEqualTo(reportDto.toReview())
        }

    @Test
    fun `getReviews should return empty list when API returns no reviews`() =
        runTest {
            // Given an empty response from the API
            coEvery { api.getReviews() } returns FoodReviewDto(emptyList())

            // When
            val result = repository.getReviews().first()

            // Then
            expectThat(result).isEmpty()
        }

    @Test
    fun `getReviews should throw exception when API fails`() =
        runTest {
            // Given
            val exception = RuntimeException("Network error")
            coEvery { api.getReviews() } throws exception

            // When & Then
            assertFailsWith<RuntimeException> {
                repository.getReviews().first()
            }
        }
}
