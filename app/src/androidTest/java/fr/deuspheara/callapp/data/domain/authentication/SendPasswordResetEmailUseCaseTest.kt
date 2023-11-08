package fr.deuspheara.callapp.data.domain.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import fr.deuspheara.callapp.core.firebase.cleanBeforeTesting
import fr.deuspheara.callapp.core.model.CoreModelProvider
import fr.deuspheara.callapp.data.repository.authentication.AuthenticationRepository
import fr.deuspheara.callapp.domain.authentication.ResetPasswordUseCase
import fr.deuspheara.callapp.domain.authentication.SendPasswordResetEmailUseCase
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.domain.authentication.SendPasswordResetEmailUseCaseTest
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Test [SendPasswordRestEmailUseCase]
 *
 */
@HiltAndroidTest
class SendPasswordResetEmailUseCaseTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var testDispatcher: TestDispatcher

    @Inject
    lateinit var firestore: FirebaseFirestore

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var repository: AuthenticationRepository

    @Inject
    lateinit var useCase: SendPasswordResetEmailUseCase

    @Before
    fun setUp() {
        hiltRule.inject()
        runBlocking {
            firebaseAuth.cleanBeforeTesting()
            firestore.cleanBeforeTesting()
        }
    }

    @Test
    fun sendPasswordResetEmailUseCase() = runTest(testDispatcher) {
        repository.signUpWithPassword(CoreModelProvider.email, CoreModelProvider.password).first()
        val actual = useCase.invoke(CoreModelProvider.email).first() ?: false

        assertTrue(actual)
    }


}