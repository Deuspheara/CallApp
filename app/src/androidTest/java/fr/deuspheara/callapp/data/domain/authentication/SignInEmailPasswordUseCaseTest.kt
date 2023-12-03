package fr.deuspheara.callapp.data.domain.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import fr.deuspheara.callapp.core.firebase.cleanBeforeTesting
import fr.deuspheara.callapp.core.model.CoreModelProvider
import fr.deuspheara.callapp.core.model.text.Email
import fr.deuspheara.callapp.core.model.text.Password
import fr.deuspheara.callapp.domain.authentication.SignInWithEmailPasswordUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.domain.authentication.SignInEmailPasswordUseCaseTest
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Test [SignInWithEmailPasswordUseCase]
 *
 */
@HiltAndroidTest
class SignInWithEmailPasswordUseCaseTest {


    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var testDispatcher: TestDispatcher

    @Inject
    lateinit var firestore: FirebaseFirestore

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var useCase: SignInWithEmailPasswordUseCase

    @Before
    fun setUp() {
        hiltRule.inject()
        runBlocking {
            firebaseAuth.cleanBeforeTesting()
            firestore.cleanBeforeTesting()
        }
    }

    @Test
    fun invoke_success() = runTest(testDispatcher) {
        val actual =
            useCase(Email("johndoe@example.com"), Password(CoreModelProvider.password)).first()?.uid
        val expected = firebaseAuth.currentUser?.uid
        assertEquals(expected, actual)
    }


    @Test
    fun invoke_failure_badCredentials() = runTest(testDispatcher) {
        val actual = useCase(Email("johndoe@example.com"), Password("badPassword")).first()
        Assert.assertNull(actual)
    }

}