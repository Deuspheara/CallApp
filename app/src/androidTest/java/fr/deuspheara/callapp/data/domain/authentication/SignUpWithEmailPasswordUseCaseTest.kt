package fr.deuspheara.callapp.data.domain.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import fr.deuspheara.callapp.core.firebase.cleanBeforeTesting
import fr.deuspheara.callapp.core.model.CoreModelProvider
import fr.deuspheara.callapp.core.model.text.Email
import fr.deuspheara.callapp.core.model.text.Password
import fr.deuspheara.callapp.core.model.text.PhoneNumber
import fr.deuspheara.callapp.domain.authentication.SignInWithEmailPasswordUseCase
import fr.deuspheara.callapp.domain.authentication.SignUpWithEmailPasswordUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.domain.authentication.SignUpWithEmailPasswordUseCaseTest
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Test [SignUpWithEmailPasswordUseCase]
 *
 */
@HiltAndroidTest
class SignUpWithEmailPasswordUseCaseTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var testDispatcher: TestDispatcher

    @Inject
    lateinit var firestore: FirebaseFirestore

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var useCase: SignUpWithEmailPasswordUseCase

    @Before
    fun setUp() {
        hiltRule.inject()
        runBlocking {
            firebaseAuth.cleanBeforeTesting()
            firestore.cleanBeforeTesting()
        }
    }

    @Test
    fun signUpWithEmailPasswordUseCaseTest() = runTest(testDispatcher) {
        val actual = useCase.invoke(
            email = Email("johndoe@example.com"),
            firstName = "John",
            lastName = "Doe",
            profilePictureUrl = "https://image.testing/profile/testing",
            bio = "I'm a test",
            pseudonym = "John Doe",
            password = Password(CoreModelProvider.password),
        ).first()

        val expected = firebaseAuth.currentUser?.uid

        assertEquals(expected, actual)
    }
}