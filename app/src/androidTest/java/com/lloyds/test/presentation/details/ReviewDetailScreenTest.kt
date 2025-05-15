package com.lloyds.test.presentation.details

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lloyds.test.domain.model.Review
import com.lloyds.test.presentation.details.ui.ReviewDetailScreenContent
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReviewDetailScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val sampleReview =
        Review(
            product = "Product ABC",
            manufacturer = "Acme Corp",
            category = "Gadgets",
            rating = 8.1,
            dateReleased = "2025-04-01",
            videoCode = "IukTiqAqVBM",
            videoTitle = "Sample Review Video",
            id = "1234",
        )

    @Before
    fun setUp() {
        // Initialize Espresso-Intents so we can verify outgoing Intents
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun loadingState_showsProgressIndicator() {
        composeTestRule.setContent {
            ReviewDetailScreenContent(
                state =
                    ReviewDetailState(
                        review = null,
                        isLoading = true,
                        error = null,
                    ),
                onBackPressed = { },
                onPlayVideo = { },
            )
        }
        composeTestRule.onNodeWithTag("loading_view").assertIsDisplayed()
    }

    @Test
    fun errorState_showsErrorMessage() {
        val errorMessage = "Network Error"
        composeTestRule.setContent {
            ReviewDetailScreenContent(
                state =
                    ReviewDetailState(
                        review = null,
                        isLoading = false,
                        error = errorMessage,
                    ),
                onBackPressed = { },
                onPlayVideo = { },
            )
        }
        composeTestRule.onNodeWithTag("error_view").assertIsDisplayed()
        composeTestRule.onNodeWithTag("error_view").assertTextEquals(errorMessage)
    }

    @Test
    fun detailScreen_displaysAllFields() {
        composeTestRule.setContent {
            ReviewDetailScreenContent(
                state =
                    ReviewDetailState(
                        review = sampleReview,
                        isLoading = false,
                        error = null,
                    ),
                onBackPressed = { },
                onPlayVideo = { },
            )
        }

        // Title in top bar (first of two matching nodes)
        composeTestRule.onAllNodesWithText("Product ABC")[0].assertIsDisplayed()

        // Video title
        composeTestRule.onNodeWithText("Sample Review Video").assertIsDisplayed()

        // Detail rows
        composeTestRule.onNodeWithText("Product").assertIsDisplayed()
        // Detail row value (second matching node)
        composeTestRule.onAllNodesWithText("Product ABC")[1].assertIsDisplayed()
        composeTestRule.onNodeWithText("Manufacturer").assertIsDisplayed()
        composeTestRule.onNodeWithText("Acme Corp").assertIsDisplayed()
        composeTestRule.onNodeWithText("Category").assertIsDisplayed()
        composeTestRule.onNodeWithText("Gadgets").assertIsDisplayed()
        composeTestRule.onNodeWithText("Rating").assertIsDisplayed()
        composeTestRule.onNodeWithText("8.1/10").assertIsDisplayed()
        composeTestRule.onNodeWithText("Release Date").assertIsDisplayed()
        composeTestRule.onNodeWithText("2025-04-01").assertIsDisplayed()
    }

    @Test
    fun backButton_triggersOnBackClick() {
        var backClicked = false
        composeTestRule.setContent {
            ReviewDetailScreenContent(
                state =
                    ReviewDetailState(
                        review = sampleReview,
                        isLoading = false,
                        error = null,
                    ),
                onBackPressed = { backClicked = true },
                onPlayVideo = { },
            )
        }

        composeTestRule.onNodeWithContentDescription("Back").performClick()

        assert(backClicked)
    }
}
