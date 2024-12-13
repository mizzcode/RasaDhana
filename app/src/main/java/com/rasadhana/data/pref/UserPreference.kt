package com.rasadhana.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.rasadhana.data.local.entity.UserEntity

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserEntity) {
        dataStore.edit { preferences ->
            preferences[ID_KEY] = user.id
            preferences[NAME_KEY] = user.name
            preferences[EMAIL_KEY] = user.email
            preferences[TOKEN_KEY] = user.token
            preferences[IS_LOGIN_KEY] = user.isLogin
            preferences[PHOTO_USER] = user.photoUrl
            preferences[EXPIRE_TOKEN] = user.expireToken
            preferences[AUTH_TYPE] = user.authType
        }
    }

    fun getSession(): Flow<UserEntity> {
        return dataStore.data.map { preferences ->
            UserEntity(
                id = preferences[ID_KEY] ?: "",
                name = preferences[NAME_KEY] ?: "",
                email = preferences[EMAIL_KEY] ?: "",
                token = preferences[TOKEN_KEY] ?: "",
                isLogin = preferences[IS_LOGIN_KEY] ?: false,
                photoUrl = preferences[PHOTO_USER] ?: "",
                expireToken = preferences[EXPIRE_TOKEN] ?: "",
                authType = preferences[AUTH_TYPE] ?: "nodejs"
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val ID_KEY = stringPreferencesKey("id")
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")
        private val PHOTO_USER = stringPreferencesKey("photo")
        private val EXPIRE_TOKEN = stringPreferencesKey("expireToken")
        private val AUTH_TYPE = stringPreferencesKey("authType")
    }
}