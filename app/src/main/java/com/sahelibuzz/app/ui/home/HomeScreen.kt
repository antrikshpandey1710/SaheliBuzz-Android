package com.sahelibuzz.app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.sahelibuzz.app.data.models.Post

@Composable
fun HomeScreen(
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: HomeViewModel = remember { HomeViewModel() }
) {
    val state = viewModel.uiState

    LaunchedEffect(Unit) {
        if (state.posts.isEmpty()) {
            viewModel.loadInitialPosts()
        }
    }

    when {
        state.isLoading -> {
            LoadingState()
        }

        state.errorMessage != null && state.posts.isEmpty() -> {
            ErrorState(
                message = state.errorMessage,
                onRetry = {
                    viewModel.clearError()
                    viewModel.loadInitialPosts()
                }
            )
        }

        state.posts.isEmpty() -> {
            EmptyState()
        }

        else -> {
            PostFeed(
                posts = state.posts,
                contentPadding = contentPadding,
                isLoadingMore = state.isLoadingMore,
                hasMore = state.hasMore,
                onLoadMore = {
                    viewModel.loadMorePosts()
                }
            )
        }
    }
}

@Composable
private fun PostFeed(
    posts: List<Post>,
    contentPadding: PaddingValues,
    isLoadingMore: Boolean,
    hasMore: Boolean,
    onLoadMore: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = contentPadding.calculateStartPadding(LayoutDirection.Ltr),
            top = contentPadding.calculateTopPadding() + 12.dp,
            end = contentPadding.calculateEndPadding(LayoutDirection.Ltr),
            bottom = contentPadding.calculateBottomPadding() + 12.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = posts,
            key = { post -> post.id }
        ) { post ->

            PostItem(
                post = post
            )

            if (post == posts.lastOrNull() && hasMore) {
                LaunchedEffect(post.id) {
                    onLoadMore()
                }
            }
        }

        if (isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun PostItem(
    post: Post
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 10.dp
            )
    ) {
        Text(
            text = if (post.username.isNotBlank()) {
                post.username
            } else {
                "SaheliBuzz User"
            },
            style = MaterialTheme.typography.titleMedium
        )

        if (post.content.isNotBlank()) {
            Text(
                text = post.content,
                modifier = Modifier.padding(top = 6.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Text(
            text = "❤️ ${post.likesCount}    💬 ${post.commentsCount}",
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Abhi koi post nahi hai.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge
        )

        Button(
            onClick = onRetry,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Retry")
        }
    }
}
