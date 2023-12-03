package fr.deuspheara.callapp.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.PersistentCacheSettings
import com.google.firebase.firestore.ktx.firestoreSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.Locale
import javax.inject.Qualifier

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.firebase.FirebaseModule
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Module for firebase instance
 *
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class UserCollectionReference {
        companion object {
            const val COLLECTION_PATH = "users"
        }
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class UserPublicCollectionReference {
        companion object {
            const val COLLECTION_PATH = "users_public"
        }
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ChannelsCollectionReference {
        companion object {
            const val COLLECTION_PATH = "channels"
        }
    }

    @Provides
    fun providesFirebaseAuthentication(): FirebaseAuth {
        return FirebaseAuth.getInstance().apply {
            this.setLanguageCode(Locale.FRANCE.language)
        }
    }


    @Provides
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance().apply {
            this.firestoreSettings = firestoreSettings {
                this.setLocalCacheSettings(PersistentCacheSettings.newBuilder().build())
            }
        }
    }

    @Provides
    @UserCollectionReference
    fun provideUserFirestoreCollectionReference(
        firestore: FirebaseFirestore,
    ): CollectionReference {
        return firestore.collection(UserCollectionReference.COLLECTION_PATH)
    }

    @Provides
    @UserPublicCollectionReference
    fun provideUserPublicFirestoreCollectionReference(
        firestore: FirebaseFirestore,
    ): CollectionReference {
        return firestore.collection(UserPublicCollectionReference.COLLECTION_PATH)
    }

    @Provides
    @ChannelsCollectionReference
    fun provideChannelsFirestoreCollectionReference(
        firestore: FirebaseFirestore,
    ): CollectionReference {
        return firestore.collection(ChannelsCollectionReference.COLLECTION_PATH)
    }


}
