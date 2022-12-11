package com.holudi.moviewatchlist.injection.modules

import com.holudi.moviewatchlist.injection.DefaultDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * For Reference
 * https://medium.com/androiddevelopers/create-an-application-coroutinescope-using-hilt-dd444e721528
 */

@InstallIn(SingletonComponent::class)
@Module
object CoroutinesModule {

    @Singleton
    @ApplicationScope
    @Provides
    fun providesCoroutineScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)

}


// this exists mainly for readability
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope