package fr.deuspheara.callapp.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.PersistentCacheSettings
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.firestore.ktx.snapshots
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
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
            const val COLLECTION_PATH = "user"
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


}
