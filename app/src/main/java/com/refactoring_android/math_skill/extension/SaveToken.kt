package com.refactoring_android.math_skill.extension

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.refactoring_android.math_skill.data.response.UserInfoResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.math.log

val Context.dataStore by preferencesDataStore(name = "auth_prefs")

object TokenDataStore {
    private val TOKEN_KEY = stringPreferencesKey("api_token")

    suspend fun saveToken(context: Context, token: String,  userInfo: UserInfoResponse) {
        println("으아아123123: $token")
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[stringPreferencesKey("branch")] = userInfo.branch?.name ?: ""

        }
    }

    suspend fun getToken(context: Context): String? {
        return context.dataStore.data
            .map { preferences -> preferences[TOKEN_KEY] }
            .first()
    }

    suspend fun clearToken(context: Context) {
        context.dataStore.edit { it.clear() }
    }


    suspend fun getInfo(context: Context): String? {
        return context.dataStore.data
            .map { preferences -> preferences[stringPreferencesKey("branch")] }
            .first()
    }

    suspend fun clearInfo(context: Context) {
        context.dataStore.edit { it.clear() }
    }
}