package fr.deuspheara.callapp.data.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.deuspheara.callapp.data.database.room.CallAppDatabase
import javax.inject.Singleton

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.database.DatabaseModule
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 *
 *
 */
@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideCallAppDatabase(
        @ApplicationContext context: Context
    ): CallAppDatabase {
        return Room.databaseBuilder(
            context,
            CallAppDatabase::class.java,
            CallAppDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }
}