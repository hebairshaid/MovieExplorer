package com.movieexplorer.auth.data.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SecureSessionManager(context: Context) {

    private val masterKey = MasterKey.Builder(context) //This creates a main encryption key
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)  //AES256 = strong encryption algorithm
        .build()                                       //GCM = secure encryption mode

    private val prefs = EncryptedSharedPreferences.create( //It creates a secure SharedPreferences database,Encrypted = locked with AES encryption
        context,
        "secure_session",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, //Keys are encrypted (AES256_SIV)
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM //Values are encrypted (AES256_GCM)
    ) //So both token name and value are protected

    fun saveToken(token: String) {
        prefs.edit().putString("token", token).apply()
    }

    fun getToken(): Flow<String?> {
        return flow {
            emit(prefs.getString("token", null))
        }// Flow mean if value changes, app can react automatically
    }

    fun clearToken() {
        prefs.edit().clear().apply()
    }
}