package fr.deuspheara.callapp.data.datasource

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.deuspheara.callapp.data.datasource.authentication.remote.AuthenticationRemoteDataSource
import fr.deuspheara.callapp.data.datasource.authentication.remote.impl.AuthenticationRemoteDataSourceImpl
import fr.deuspheara.callapp.data.datasource.user.remote.UserRemoteDataSource
import fr.deuspheara.callapp.data.datasource.user.remote.impl.UserRemoteDataSourceImpl

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.datasource.DatasourceModule
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Module to provide datasource
 *
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindsAuthenticationRemoteDataSource(
        impl: AuthenticationRemoteDataSourceImpl,
    ): AuthenticationRemoteDataSource

    @Binds
    abstract fun bindsUserRemoteDataSource(
        impl: UserRemoteDataSourceImpl,
    ): UserRemoteDataSource
}