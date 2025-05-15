package com.lloyds.test.presentation

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.lloyds.test.presentation.constants.Screens
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

// Make sure you have a NavigationGraph(navController: NavHostController) composable
// and a sealed Screens object with Reviews and Detail routes.
class NavigationGraphTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun startDestinationIsReviewsScreen() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        composeTestRule.setContent {
            NavigationGraph(navController)
        }

        // Immediately after setContent, the start destination should be loaded
        Assert.assertEquals(
            Screens.Reviews.route,
            navController.currentDestination?.route
        )
    }

    @Test
    fun navigatingToDetailUpdatesCurrentDestination() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        composeTestRule.setContent {
            NavigationGraph(navController)
        }

        // Perform navigation
        val sampleId = "sample123"
        composeTestRule.runOnIdle {
            navController.navigate("detail/$sampleId")
        }

        // Verify the route has changed
        Assert.assertEquals(
            Screens.Detail.route,
            navController.currentDestination?.route
        )
    }

    @Test
    fun detailArgumentIsPassedCorrectly() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        composeTestRule.setContent {
            NavigationGraph(navController)
        }

        // Navigate with an argument
        val sampleId = "abc"
        composeTestRule.runOnIdle {
            navController.navigate("detail/$sampleId")
        }

        // Verify the argument is in the back stack
        val backStackEntry = navController.currentBackStackEntry
        val arg = backStackEntry?.arguments?.getString("reviewId")
        Assert.assertEquals(sampleId, arg)
    }
}