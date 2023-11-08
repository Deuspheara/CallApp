package fr.deuspheara.callapp.core.model.text

import android.util.Patterns

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.core.model.text.Email
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Email Implementation.
 *
 * This class serves as a value type that wraps around a string meant to represent an email.
 * It provides utility to validate the email against common patterns.
 *
 * Instead of direct construction, use the companion object method `of` to create an instance.
 * If the email is valid, it returns an instance of `Email`. Otherwise, it returns `null`.
 *
 * ### Example Usage:
 * ```
 * val email = Email.of("example@example.com")
 * if (email?.isValid == true) {
 *     println("The email is valid.")
 * } else {
 *     println("The email is invalid.")
 * }
 * ```
 */

@JvmInline
value class Email constructor(val value: String) {
    companion object {
        fun of(value: String): Email? {
            return if (Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
                Email(value)
            } else {
                null
            }
        }
    }

    val isValid: Boolean get() = Patterns.EMAIL_ADDRESS.matcher(value).matches()
}
