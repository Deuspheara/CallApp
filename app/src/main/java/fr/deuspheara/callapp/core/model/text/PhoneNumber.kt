package fr.deuspheara.callapp.core.model.text

import android.util.Patterns

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.core.model.text.PhoneNumber
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Phone number model
 *
 */
@JvmInline
value class PhoneNumber(val value: String = String()) {
    val isValid: Boolean get() = Patterns.PHONE.matcher(value).matches() || value.isEmpty()

}