package com.lloyds.test.presentation.details.ui

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.lloyds.test.R
import com.lloyds.test.domain.model.Review
import com.lloyds.test.presentation.details.ReviewDetailState
import com.lloyds.test.presentation.details.ReviewDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewDetailScreenContent(
    state: ReviewDetailState,
    onBackPressed: () -> Unit,
    onPlayVideo: (String) -> Unit,
) {
    val reviewTitle = state.review?.product ?: stringResource(id = R.string.title_product_review)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(reviewTitle) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back),
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier =
                            Modifier
                                .align(Alignment.Center)
                                .testTag("loading_view"),
                    )
                }
            }

            state.error != null -> {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        modifier =
                            Modifier
                                .align(Alignment.Center)
                                .testTag("error_view"),
                        text = state.error,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }

            state.review != null -> {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                ) {
                    ReviewContent(
                        modifier = Modifier.fillMaxSize(),
                        review = state.review,
                        onPlayVideo,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewDetailScreen(
    reviewId: String,
    viewModel: ReviewDetailViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
) {
    LaunchedEffect(reviewId) {
        viewModel.getReview(reviewId)
    }
    val state by viewModel.state.collectAsState()
    // Provide context and navigation logic here:
    val context = LocalContext.current
    ReviewDetailScreenContent(
        state = state,
        onBackPressed = onBackPressed,
        onPlayVideo = { videoCode ->
            try {
                val intent = Intent(Intent.ACTION_VIEW, "vnd.youtube:$videoCode".toUri())
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                } else {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            "https://www.youtube.com/watch?v=$videoCode".toUri(),
                        ),
                    )
                }
            } catch (e: Exception) {
                Toast
                    .makeText(context, "Could not open YouTube: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        },
    )
}

@Composable
fun ReviewContent(
    modifier: Modifier,
    review: Review,
    onPlayVideo: (String) -> Unit,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
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

            // Play button overlay
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center,
            ) {
                IconButton(
                    modifier = Modifier.align(Alignment.Center),
                    onClick = { onPlayVideo(review.videoCode) },
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        modifier =
                            Modifier
                                .size(64.dp)
                                .scale(3f)
                                .padding(8.dp),
                        tint = Color.White,
                    )
                }
            }
        }

        // Review details
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        ) {
            Text(
                text = review.videoTitle,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(16.dp))

            DetailRow("Product", review.product)
            DetailRow("Manufacturer", review.manufacturer)
            DetailRow("Category", review.category)
            DetailRow("Rating", "${review.rating}/10")
            DetailRow("Release Date", review.dateReleased)
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}
