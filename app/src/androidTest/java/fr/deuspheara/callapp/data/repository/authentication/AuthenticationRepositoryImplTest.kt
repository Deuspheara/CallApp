package fr.deuspheara.callapp.data.repository.authentication

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import fr.deuspheara.callapp.core.firebase.cleanBeforeTesting
import fr.deuspheara.callapp.core.model.CoreModelProvider
import fr.deuspheara.callapp.data.repository.authentication.impl.AuthenticationRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.repository.authentication.AuthenticationRepositoryImplTest
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Authentication repository tests
 *
 */
@HiltAndroidTest
class AuthenticationRepositoryImplTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var testDispatcher: TestDispatcher

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var repository: AuthenticationRepositoryImpl

    @Before
    fun setUp() {
        hiltRule.inject()
        runBlocking {
            firebaseAuth.cleanBeforeTesting()
        }
    }

    @Test
    fun signUpWithEmailAndPassword_success() = runTest(testDispatcher) {
        val actual = repository.signUpWithPassword(CoreModelProvider.email, CoreModelProvider.password).first()
        val expected = firebaseAuth.currentUser?.uid!!
        Assert.assertEquals(expected, actual)
    }

    @Test(expected = FirebaseException::class)
    fun signUpWithEmailAndPassword_failure() = runTest(testDispatcher) {
        repository.signUpWithPassword(CoreModelProvider.email, CoreModelProvider.password).first()
        repository.signUpWithPassword(CoreModelProvider.email, CoreModelProvider.password).first()
    }

    @Test
    fun signInWithEmailAndPassword_success() = runTest(testDispatcher) {
        repository.signUpWithPassword(CoreModelProvider.email, CoreModelProvider.password).first()
        firebaseAuth.signOut()
        val actual = repository.signInWithPassword(CoreModelProvider.email, CoreModelProvider.password).first()
        Assert.assertEquals(firebaseAuth.currentUser!!.uid, actual)
    }

    @Test(expected = FirebaseException::class)
    fun signInWithEmailAndPassword_failure_wrongPassword() = runTest(testDispatcher) {
        repository.signUpWithPassword(CoreModelProvider.email, CoreModelProvider.password).first()
        repository.signUpWithPassword(CoreModelProvider.email, "bad_password@").first()
    }

    @Test(expected = FirebaseException::class)
    fun signInWithEmailAndPassword_failure_noUser() = runTest(testDispatcher) {
        repository.signInWithPassword(CoreModelProvider.email, CoreModelProvider.password).first()
    }

    @Test
    fun isUserAuthenticated_authenticated() = runTest(testDispatcher) {
        Assert.assertFalse(repository.isUserAuthenticated().first())
    }

    @Test
    fun isUserAuthenticated_notAuthenticated() = runTest(testDispatcher) {
        repository.signUpWithPassword(CoreModelProvider.email, CoreModelProvider.password).first()
        firebaseAuth.signOut()
        repository.signInWithPassword(CoreModelProvider.email, CoreModelProvider.password).first()
        Assert.assertTrue(repository.isUserAuthenticated().first())
    }
}