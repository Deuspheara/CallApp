package fr.deuspheara.callapp.data.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
 * Hilt Module for repository
 *
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsAuthenticationRepository(
        impl: AuthenticationRepositoryImpl,
    ): AuthenticationRepository

    @Binds
    abstract fun bindsUserRepository(
        impl: UserRepositoryImpl,
    ): UserRepository

}