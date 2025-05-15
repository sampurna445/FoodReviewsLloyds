package com.lloyds.test.data.remote.dto

import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class ReportDtoTest {
    @Test
    fun `toReport should map all fields correctly`() {
        // Given
        val dto =
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

        // When
        val result = dto.toReview()

        // Then
        expectThat(result) {
            get { id }.isEqualTo("1")
            get { product }.isEqualTo("Test Product")
            get { manufacturer }.isEqualTo("Test Manufacturer")
            get { category }.isEqualTo("Test Category")
            get { rating }.isEqualTo(8.5)
            get { dateReleased }.isEqualTo("2024-03-20")
            get { videoCode }.isEqualTo("test123")
            get { videoTitle }.isEqualTo("Test Video")
        }
    }

    @Test
    fun `toReport should handle null category`() {
        // Given
        val dto =
            ReportDto(
                category = null,
                dateReleased = "2024-03-20",
                id = "1",
                manufacturer = "Test Manufacturer",
                product = "Test Product",
                rating = 8.5,
                videoCode = "test123",
                videoTitle = "Test Video",
            )

        // When
        val result = dto.toReview()

        // Then
        expectThat(result) {
            get { category }.isEqualTo("Others")
        }
    }
} 
