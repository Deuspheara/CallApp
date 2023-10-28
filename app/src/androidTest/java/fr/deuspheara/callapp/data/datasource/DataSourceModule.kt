package fr.deuspheara.callapp.data.datasource

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import fr.deuspheara.callapp.data.datasource.authentication.remote.AuthenticationRemoteDataSource
import fr.deuspheara.callapp.data.datasource.authentication.remote.AuthenticationRemoteDataSourceFake
import fr.deuspheara.callapp.data.datasource.user.remote.UserRemoteDataSource
import fr.deuspheara.callapp.data.datasource.user.remote.impl.UserRemoteDataSourceImpl

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.datasource.DataSourceModule
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Data source fake module
 *
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataSourceModule::class]
)
abstract class DataSourceTestModule {

    @Binds
    abstract fun bindAuthRemoteDataSource(
        impl: AuthenticationRemoteDataSourceFake
    ): AuthenticationRemoteDataSource

    @Binds
    abstract fun bindUserLocalDataSource(
        impl: UserRemoteDataSourceImpl
    ): UserRemoteDataSource
}