package com.sahelibuzz.app.data.models

data class Post(
    val id: String = "",
    val userId: String = "",
    val username: String = "",
    val userPhotoURL: String? = null,
    val content: String = "",
    val imageURL: String? = null,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val reactions: Map<String, Int> = emptyMap(),
    val createdAt: String = "",
    val updatedAt: String? = null,
    val isAnonymous: Boolean = false,

    val isPoll: Boolean = false,
    val pollOptions: List<String> = emptyList(),
    val pollVotes: Map<String, List<String>> = emptyMap(),
    val pollExpiryDuration: String? = null,
    val pollExpiryAt: String? = null,

    val type: String = "post",
    val originalPostId: String? = null,
    val originalAuthorId: String? = null,
    val originalAuthorName: String? = null,
    val repostedBy: String? = null,
    val quoteText: String? = null,
    val quotedByUserId: String? = null,
    val rebuzzesCount: Int = 0,
    val quotesCount: Int = 0
)
