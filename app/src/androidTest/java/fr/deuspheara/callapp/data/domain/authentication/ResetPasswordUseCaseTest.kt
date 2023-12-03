package fr.deuspheara.callapp.data.domain.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import fr.deuspheara.callapp.core.firebase.cleanBeforeTesting
import fr.deuspheara.callapp.core.model.CoreModelProvider
import fr.deuspheara.callapp.data.repository.authentication.AuthenticationRepository
import fr.deuspheara.callapp.domain.authentication.ResetPasswordUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.domain.authentication.ResetPasswordUseCaseTest
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Test [ResetPassworUseCase]
 *
 */

@HiltAndroidTest
class ResetPasswordUseCaseTest {

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
    lateinit var useCase: ResetPasswordUseCase

    @Before
    fun setUp() {
        hiltRule.inject()
        runBlocking {
            firebaseAuth.cleanBeforeTesting()
            firestore.cleanBeforeTesting()
        }
    }

    @Test
    fun resetPassword_success() = runTest(testDispatcher) {
        val actual = useCase.invoke(CoreModelProvider.email).first() ?: Instant.MAX
        assertTrue(actual.isBefore(Instant.MAX))
    }

}