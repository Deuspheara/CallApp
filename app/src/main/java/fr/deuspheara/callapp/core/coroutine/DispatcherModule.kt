package fr.deuspheara.callapp.core.coroutine

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.core.coroutine.DispatcherModule
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Dispatcher module
 *
 */
@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class IoDispatcher

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DefaultDispatcher

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MainDispatcher

    @Provides
    @IoDispatcher
    fun providesDispatcherIO(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    @DefaultDispatcher
    fun providesDispatcherDefault(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    @Provides
    @MainDispatcher
    fun providesDispatcherMain(): CoroutineDispatcher {
        return Dispatchers.Main
    }

}