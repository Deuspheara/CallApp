package fr.deuspheara.callapp

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.CallAppRunner
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Call app runner
 *
 */
class CallAppRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}