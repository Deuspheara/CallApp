package fr.deuspheara.callapp.data.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import fr.deuspheara.callapp.data.repository.authentication.AuthenticationRepository
import fr.deuspheara.callapp.data.repository.authentication.impl.AuthenticationRepositoryImpl
import fr.deuspheara.callapp.data.repository.user.UserRepository
import fr.deuspheara.callapp.data.repository.user.impl.UserRepositoryImpl

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.repository.RepositoryModule
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Fake Repository module
 *
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class RepositoryTestModule {
    @Binds
    abstract fun bindAuthRepository(
        fakeImpl: AuthenticationRepositoryImpl
    ): AuthenticationRepository

    @Binds
    abstract fun bindUserRepository(
        fakeImpl: UserRepositoryImpl
    ): UserRepository

}