package com.lloyds.test.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lloyds.test.presentation.constants.Screens
import com.lloyds.test.presentation.details.ui.ReviewDetailScreen
import com.lloyds.test.presentation.reviews.ui.ReviewsScreen
import com.lloyds.test.presentation.theme.LloydsTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReviewApp()
        }
    }
}

@Composable
fun ReviewApp() {
    val navController = rememberNavController()
    LloydsTestTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            NavigationGraph(navController)
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.Reviews.route,
    ) {
        composable(route = Screens.Reviews.route) {
            ReviewsScreen(navController = navController)
        }
        composable(
            route = Screens.Detail.route,
            arguments = listOf(navArgument("reviewId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val reviewId = backStackEntry.arguments!!.getString("reviewId")!!
            ReviewDetailScreen(
                reviewId,
                onBackPressed = {
                    navController.popBackStack()
                },
            )
        }
    }
}
