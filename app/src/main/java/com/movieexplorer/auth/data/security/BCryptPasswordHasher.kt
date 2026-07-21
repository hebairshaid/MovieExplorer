package com.movieexplorer.auth.data.security

import com.movieexplorer.auth.domain.security.PasswordHasher
import org.mindrot.jbcrypt.BCrypt

class BCryptPasswordHasher : PasswordHasher {

    override fun hash(password: String): String {
        return BCrypt.hashpw(password.trim(), BCrypt.gensalt())
    }

    override fun verify(plainPassword: String, hashedPassword: String): Boolean {
        return try {
            if (hashedPassword.isBlank()) return false
            BCrypt.checkpw(plainPassword.trim(), hashedPassword)
        } catch (e: Exception) {
            false
        }
    }
}
