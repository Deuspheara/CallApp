package fr.deuspheara.callapp.data.datasource

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.deuspheara.callapp.data.datasource.agora.AgoraDataSource
import fr.deuspheara.callapp.data.datasource.agora.impl.AgoraDatasourceImpl
import fr.deuspheara.callapp.data.datasource.authentication.local.AuthenticationLocalDataSource
import fr.deuspheara.callapp.data.datasource.authentication.local.impl.AuthenticationLocalDataSourceImpl
import fr.deuspheara.callapp.data.datasource.authentication.remote.AuthenticationRemoteDataSource
import fr.deuspheara.callapp.data.datasource.authentication.remote.impl.AuthenticationRemoteDataSourceImpl
import fr.deuspheara.callapp.data.datasource.channels.video.remote.ChannelsDatasource
import fr.deuspheara.callapp.data.datasource.channels.video.remote.impl.ChannelsDatasourceImpl
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

    @Binds
    abstract fun bindsAuthenticationLocalDataSource(
        impl: AuthenticationLocalDataSourceImpl,
    ): AuthenticationLocalDataSource

    @Binds
    abstract fun bindsAgoraDataSource(
        impl: AgoraDatasourceImpl,
    ): AgoraDataSource

    @Binds
    abstract fun bindsChannelsDataSource(
        impl: ChannelsDatasourceImpl,
    ): ChannelsDatasource
}