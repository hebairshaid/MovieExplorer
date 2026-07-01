/*package com.movieexplorer.auth.data.security

import com.movieexplorer.auth.domain.security.PasswordHasher
import java.security.MessageDigest

class Sha256PasswordHasher : PasswordHasher {

    override fun hash(password: String): String {
        val bytes = MessageDigest
            .getInstance("SHA-256")
            .digest(password.toByteArray()) //converts password → encrypted hash bytes
        //MessageDigest.getInstance("SHA-256") creates a hashing algorithm

        return bytes.joinToString("") {
            "%02x".format(it) //converts bytes → readable hex string
        }
    }

    override fun verify(
        plainPassword: String,
        hashedPassword: String
    ): Boolean {
        return hash(plainPassword) == hashedPassword
    }
}*/