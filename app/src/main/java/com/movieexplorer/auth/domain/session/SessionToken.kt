package com.movieexplorer.auth.domain.session

object SessionToken {
    private const val PREFIX = "local_token_"

    fun create(email: String): String {
        return "$PREFIX${email.trim().lowercase()}"
    }

    fun emailFrom(token: String?): String {
        if (token.isNullOrBlank() || !token.startsWith(PREFIX)) return ""
        return token.removePrefix(PREFIX).trim().lowercase()
    }
}
