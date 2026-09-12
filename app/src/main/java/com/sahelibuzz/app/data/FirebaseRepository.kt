package com.sahelibuzz.app.data

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.sahelibuzz.app.data.models.Post

data class PostPage(
    val posts: List<Post>,
    val lastDocument: DocumentSnapshot?,
    val hasMore: Boolean
)

class FirebaseRepository {

    private val db = FirebaseFirestore.getInstance()

    private val postsCollection = db.collection("posts")

    fun loadLatestPosts(
        onResult: (Result<PostPage>) -> Unit
    ) {
        postsCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(PAGE_SIZE)
            .get()
            .addOnSuccessListener { snapshot ->

                val posts = snapshot.documents.mapNotNull { document ->
                    document.toObject(Post::class.java)?.copy(
                        id = document.id
                    )
                }

                val lastDocument = snapshot.documents.lastOrNull()

                onResult(
                    Result.success(
                        PostPage(
                            posts = posts,
                            lastDocument = lastDocument,
                            hasMore = snapshot.size() == PAGE_SIZE
                        )
                    )
                )
            }
            .addOnFailureListener { error ->
                onResult(Result.failure(error))
            }
    }

    fun loadMorePosts(
        lastDocument: DocumentSnapshot,
        onResult: (Result<PostPage>) -> Unit
    ) {
        postsCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .startAfter(lastDocument)
            .limit(PAGE_SIZE)
            .get()
            .addOnSuccessListener { snapshot ->

                val posts = snapshot.documents.mapNotNull { document ->
                    document.toObject(Post::class.java)?.copy(
                        id = document.id
                    )
                }

                val newLastDocument = snapshot.documents.lastOrNull()

                onResult(
                    Result.success(
                        PostPage(
                            posts = posts,
                            lastDocument = newLastDocument,
                            hasMore = snapshot.size() == PAGE_SIZE
                        )
                    )
                )
            }
            .addOnFailureListener { error ->
                onResult(Result.failure(error))
            }
    }

    companion object {
        private const val PAGE_SIZE = 20L
    }
}
