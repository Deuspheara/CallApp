package fr.deuspheara.callapp.data.domain.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import fr.deuspheara.callapp.core.firebase.cleanBeforeTesting
import fr.deuspheara.callapp.data.repository.authentication.AuthenticationRepository
import fr.deuspheara.callapp.domain.authentication.ConfirmResetPasswordUseCase
import junit.framework.TestCase.assertFalse
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
 * fr.deuspheara.callapp.data.domain.authentication.ConfirmResetPasswordUseCaseTest
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Test Confirm reset password UseCase
 *
 */
@HiltAndroidTest
class ConfirmResetPasswordUseCaseTest {

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
    lateinit var useCase: ConfirmResetPasswordUseCase

    @Before
    fun setUp() {
        hiltRule.inject()
        runBlocking {
            firebaseAuth.cleanBeforeTesting()
            firestore.cleanBeforeTesting()
        }
    }

    @Test
    fun invoke_wrongCode() = runTest(testDispatcher) {
        assertFalse(useCase.invoke("testing", "password").first())
    }

}