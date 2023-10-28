package fr.deuspheara.callapp.core.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import fr.deuspheara.callapp.data.firebase.FirebaseModule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.core.firebase.FirebaseModule
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Firebase emulator module
 *
 */

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [FirebaseModule::class])
class FirebaseModuleTest {
    @Provides
    fun providesFirebaseAuthentication(): FirebaseAuth {
        return FirebaseAuth.getInstance().apply {
            try {
                useEmulator("10.0.2.2", 9099)
            } catch (_: Exception) {
            }
        }
    }

    @Provides
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance().apply {
            try {
                useEmulator("10.0.2.2", 8080)
            } catch (_: Exception) {
            }
        }
    }

    @Provides
    @FirebaseModule.UserCollectionReference
    fun provideUserFirestoreCollectionReference(
        firestore: FirebaseFirestore,
    ): CollectionReference {
        return firestore.collection(FirebaseModule.UserCollectionReference.COLLECTION_PATH)
    }

}


suspend fun FirebaseAuth.cleanBeforeTesting() {
    try {
        this@cleanBeforeTesting.signInWithEmailAndPassword("callapp@gmail.com", "password").await()
    } catch (_: java.lang.Exception) {
    }
    this@cleanBeforeTesting.currentUser?.delete()?.await()
    this@cleanBeforeTesting.signOut()
}

suspend fun FirebaseFirestore.cleanBeforeTesting() {
    with(this.batch()) {
        this@cleanBeforeTesting.collection("user").snapshots().first()
            .forEach { this.delete(it.reference) }
    }
    this.clearPersistence()
}
