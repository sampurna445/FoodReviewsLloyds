package com.lloyds.test.presentation.reviews

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import com.lloyds.test.domain.model.Review
import org.junit.Rule
import org.junit.Test
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import com.lloyds.test.presentation.reviews.ui.ReviewsScreenContent

class ReviewsScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun loadingState_showsProgressIndicator() {
        composeTestRule.setContent {
            val navController: NavHostController = rememberNavController()
            ReviewsScreenContent(
                state = ReviewsState(
                    reviews = emptyList(),
                    isLoading = true,
                    error = null
                ),
                onEvent = {},
                navController = navController
            )
        }
        composeTestRule.onNodeWithTag("loading_view").assertIsDisplayed()
    }

    @Test
    fun errorState_showsErrorMessage() {
        val errorMessage = "Network Error"
        composeTestRule.setContent {
            val navController: NavHostController = rememberNavController()
            ReviewsScreenContent(
                state = ReviewsState(
                    reviews = emptyList(),
                    isLoading = false,
                    error = errorMessage
                ),
                onEvent = {},
                navController = navController
            )
        }
        composeTestRule.onNodeWithTag("error_view").assertIsDisplayed()
        composeTestRule.onNodeWithTag("error_view").assertTextEquals(errorMessage)
    }

    @Test
    fun showsNoReviewsMessage() {
        composeTestRule.setContent {
            val navController: NavHostController = rememberNavController()
            ReviewsScreenContent(
                state = ReviewsState(reviews = emptyList()),
                onEvent = {},
                navController = navController
            )
        }
        composeTestRule.onNodeWithText("No reviews available").assertIsDisplayed()
    }

    @Test
    fun showsReviewItem() {
        val review = Review(
            id = "1",
            product = "Test Product",
            manufacturer = "Test Manufacturer",
            category = "Test Category",
            rating = 4.5,
            dateReleased = "2024-01-01",
            videoCode = "video123",
            videoTitle = "Test Video"
        )
        composeTestRule.setContent {
            val navController: NavHostController = rememberNavController()
            ReviewsScreenContent(
                state = ReviewsState(reviews = listOf(review)),
                onEvent = {},
                navController = navController
            )
        }

        composeTestRule.onNodeWithText("Test Manufacturer").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Category").assertIsDisplayed()
        composeTestRule.onNodeWithText("Rating: 4.5/10").assertIsDisplayed()
    }

}