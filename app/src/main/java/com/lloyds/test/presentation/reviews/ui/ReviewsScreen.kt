package com.lloyds.test.presentation.reviews.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.lloyds.test.R
import com.lloyds.test.domain.model.Review
import com.lloyds.test.presentation.constants.Screens
import com.lloyds.test.presentation.reviews.ReviewsIntent
import com.lloyds.test.presentation.reviews.ReviewsState
import com.lloyds.test.presentation.reviews.ReviewsViewModel

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsScreenContent(
    state: ReviewsState,
    onEvent: (ReviewsIntent) -> Unit,
    navController: NavHostController,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Food Reviews",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            FilterSection(
                selectedCategory = state.selectedCategory,
                minRating = state.minRating,
                maxRating = state.maxRating,
                onCategorySelected = { category ->
                    onEvent(ReviewsIntent.UpdateCategory(category))
                },
                onMinRatingChanged = { rating ->
                    onEvent(ReviewsIntent.UpdateMinRating(rating))
                },
                onMaxRatingChanged = { rating ->
                    onEvent(ReviewsIntent.UpdateMaxRating(rating))
                },
            )

            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(
                            modifier =
                                Modifier
                                    .align(Alignment.Center)
                                    .testTag("loading_view"),
                        )
                    }

                    state.error != null -> {
                        Text(
                            modifier =
                                Modifier
                                    .align(Alignment.Center)
                                    .testTag("error_view"),
                            text = state.error,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }

                    else -> {
                        if (state.reviews.isEmpty()) {
                            Text(
                                text = stringResource(R.string.no_reviews_available),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.align(Alignment.Center),
                            )
                        } else {
                            LazyColumn(
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                items(state.reviews) { review ->
                                    ReviewItem(
                                        review = review,
                                        onClick = {
                                            navController.navigate(
                                                Screens.Detail.createRoute(review.id),
                                            )
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewsScreen(
    viewModel: ReviewsViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val state by viewModel.state.collectAsState()
    ReviewsScreenContent(state, viewModel::onIntent, navController)
}

@Composable
fun FilterSection(
    selectedCategory: String?,
    minRating: Int?,
    maxRating: Int?,
    onCategorySelected: (String?) -> Unit,
    onMinRatingChanged: (Int?) -> Unit,
    onMaxRatingChanged: (Int?) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                OutlinedTextField(
                    value = selectedCategory ?: "All Categories",
                    onValueChange = { },
                    readOnly = true,
                    enabled = false,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select Category",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                )

                Box(
                    modifier =
                        Modifier
                            .matchParentSize()
                            .clickable { isExpanded = true },
                )

                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                    modifier = Modifier.fillMaxWidth(0.9f),
                ) {
                    DropdownMenuItem(
                        text = { Text("All Categories") },
                        onClick = {
                            onCategorySelected(null)
                            isExpanded = false
                        },
                    )
                    listOf(
                        "Energy Crisis",
                        "Running On Empty",
                        "Other",
                        "Drink Review",
                        "Music Review",
                        "Travel Review",
                    ).forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                onCategorySelected(category)
                                isExpanded = false
                            },
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "Rating Range",
                    style =
                        MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    modifier = Modifier.padding(bottom = 12.dp),
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Button(
                        onClick = {
                            onMinRatingChanged(5)
                            onMaxRatingChanged(null)
                        },
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    if (minRating == 5) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.primaryContainer
                                    },
                                contentColor =
                                    if (minRating == 5) {
                                        MaterialTheme.colorScheme.onPrimary
                                    } else {
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    },
                            ),
                        modifier =
                            Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                    ) {
                        Text("Min Rating (5)")
                    }

                    Button(
                        onClick = {
                            onMinRatingChanged(null)
                            onMaxRatingChanged(5)
                        },
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    if (maxRating == 5) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.primaryContainer
                                    },
                                contentColor =
                                    if (maxRating == 5) {
                                        MaterialTheme.colorScheme.onPrimary
                                    } else {
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    },
                            ),
                        modifier =
                            Modifier
                                .weight(1f)
                                .padding(start = 8.dp),
                    ) {
                        Text("Max Rating (5+)")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(
                        onClick = {
                            onMinRatingChanged(null)
                            onMaxRatingChanged(null)
                        },
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                            ),
                        modifier = Modifier.fillMaxWidth(0.8f),
                    ) {
                        Text("Clear Rating Filter")
                    }
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewItem(
    review: Review,
    onClick: (review: Review) -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth(),
        onClick = { onClick(review) },
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        ) {
            // YouTube thumbnail with play button overlay
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(240.dp),
            ) {
                Image(
                    painter =
                        rememberAsyncImagePainter(
                            model = "https://img.youtube.com/vi/${review.videoCode}/maxresdefault.jpg",
                        ),
                    contentDescription = review.videoTitle,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = review.product,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = review.manufacturer,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = review.category,
                style = MaterialTheme.typography.bodyMedium,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.rating, review.rating),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = review.dateReleased,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
