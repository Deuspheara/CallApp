package fr.deuspheara.callapp.core.coroutine

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import javax.inject.Singleton

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.core.coroutine.DispatcherModuleTest
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * DIspatcher module test
 *
 */
@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DispatcherModule::class])
object DispatcherModuleTest {
    @Provides
    @Singleton
    fun providesTestScope(): TestScope {
        return TestScope()
    }

    @Provides
    @Singleton
    fun provideTestDispatcher(
        testScope: TestScope,
    ): TestDispatcher {
        return StandardTestDispatcher(
            scheduler = testScope.testScheduler,
            name = "testing_dispatcher"
        )
    }

    @Provides
    @DispatcherModule.IoDispatcher
    fun providesDispatcherIO(dispatcher: TestDispatcher): CoroutineDispatcher = dispatcher

    @Provides
    @DispatcherModule.DefaultDispatcher
    fun providesDispatcherDefault(dispatcher: TestDispatcher): CoroutineDispatcher = dispatcher

    @Provides
    @DispatcherModule.MainDispatcher
    fun providesDispatcherMain(dispatcher: TestDispatcher): CoroutineDispatcher = dispatcher
}