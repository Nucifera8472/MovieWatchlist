package com.holudi.moviewatchlist.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.holudi.moviewatchlist.data.model.UserData
import com.holudi.moviewatchlist.utils.catchAndHandleError
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Used for storing app preferences set by the user, either directly or by interacting with the app
 * (e.g. marking a favorite, playing stations)
 *
 * Last played station must be saved in json format, because their order is relevant, and reordering
 * items is not supported by string sets
 */
@Singleton
class UserDataRepository @Inject constructor(@ApplicationContext val context: Context) {

    private val Context.dataStore by preferencesDataStore(
        name = USER_DATA_NAME
    )

    private object PreferencesKeys {
        val WATCHLIST = stringSetPreferencesKey("watchlist")
        val IGNORE_LIST = stringSetPreferencesKey("ignored")
    }

    fun watchlistIdsFlow() = PreferencesKeys.WATCHLIST.watchValue(defaultValue = emptySet())
    fun ignoredIdsFlow() = PreferencesKeys.IGNORE_LIST.watchValue(defaultValue = emptySet())

    val userPreferencesFlow: Flow<UserData> =
        context.dataStore.data.catchAndHandleError().map { preferences ->
            // get all values from preferences, set defaults if not available
            val watchlistIds = preferences[PreferencesKeys.WATCHLIST] ?: emptySet()
            val ignoredIds = preferences[PreferencesKeys.IGNORE_LIST] ?: emptySet()
            UserData(ignoredIds, watchlistIds)
        }.flowOn(Dispatchers.Default)


    /**
     * Returns true when the writing was finished successfully, false otherwise
     */
    suspend fun toggleWatchlist(imdbId: String): Boolean {
        try {
            context.dataStore.edit { preferences ->
                val watchlistIds =
                    (preferences[PreferencesKeys.WATCHLIST]?.toMutableSet()
                        ?: emptySet()).toMutableSet()

                if (watchlistIds.contains(imdbId)) {
                    watchlistIds.remove(imdbId)
                } else {
                    watchlistIds.add(imdbId)
                }
                preferences[PreferencesKeys.WATCHLIST] = watchlistIds
            }
            return true
        } catch (e: Exception) {
            return false
        }
    }

    suspend fun setMediaIgnored(imdbId: String): Boolean {
        return try {
            context.dataStore.edit { preferences ->
                val ignoredMedia =
                    (preferences[PreferencesKeys.IGNORE_LIST]?.toMutableSet()
                        ?: emptySet()).toMutableSet()
                ignoredMedia.add(imdbId)
                preferences[PreferencesKeys.IGNORE_LIST] = ignoredMedia
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun <T, M> Preferences.Key<T>.watchValue(convertTo: (T?) -> M): Flow<M> {
        return context.dataStore.data
            .catchAndHandleError()
            .map { preferences -> preferences[this] }.flowOn(Dispatchers.Default)
            .map(convertTo).flowOn(Dispatchers.Default)
    }

    private fun <T> Preferences.Key<T>.watchValue(defaultValue: T): Flow<T> {
        return context.dataStore.data
            .catchAndHandleError()
            .map { preferences -> preferences[this] ?: defaultValue }.flowOn(Dispatchers.Default)
    }

    companion object {
        private const val USER_DATA_NAME = "user_data"
    }
}