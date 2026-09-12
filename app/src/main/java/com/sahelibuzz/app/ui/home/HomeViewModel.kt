package com.sahelibuzz.app.ui.home

import androidx.lifecycle.ViewModel
import com.sahelibuzz.app.data.FirebaseRepository
import com.sahelibuzz.app.data.models.Post
import com.google.firebase.firestore.DocumentSnapshot

data class HomeUiState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMore: Boolean = true,
    val errorMessage: String? = null
)

class HomeViewModel : ViewModel() {

    private val repository = FirebaseRepository()

    var uiState = HomeUiState()
        private set

    private var lastDocument: DocumentSnapshot? = null

    fun loadInitialPosts() {
        if (uiState.isLoading) return

        uiState = uiState.copy(
            isLoading = true,
            errorMessage = null
        )

        repository.loadLatestPosts { result ->

            result.onSuccess { page ->

                lastDocument = page.lastDocument

                uiState = uiState.copy(
                    posts = page.posts,
                    isLoading = false,
                    hasMore = page.hasMore,
                    errorMessage = null
                )
            }

            result.onFailure { error ->

                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "Posts load nahi ho paaye."
                )
            }
        }
    }

    fun loadMorePosts() {
        val cursor = lastDocument ?: return

        if (uiState.isLoadingMore || !uiState.hasMore) return

        uiState = uiState.copy(
            isLoadingMore = true,
            errorMessage = null
        )

        repository.loadMorePosts(cursor) { result ->

            result.onSuccess { page ->

                lastDocument = page.lastDocument

                uiState = uiState.copy(
                    posts = uiState.posts + page.posts,
                    isLoadingMore = false,
                    hasMore = page.hasMore,
                    errorMessage = null
                )
            }

            result.onFailure { error ->

                uiState = uiState.copy(
                    isLoadingMore = false,
                    errorMessage = error.message ?: "Aur posts load nahi ho paaye."
                )
            }
        }
    }

    fun clearError() {
        uiState = uiState.copy(
            errorMessage = null
        )
    }
}
