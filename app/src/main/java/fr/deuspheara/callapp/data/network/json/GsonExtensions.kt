package fr.deuspheara.callapp.data.network.json

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.network.json.GsonExtensions
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Gson extensions
 *
 */

inline fun <reified T> Gson.parse(input: String): T {
    return this.fromJson(input, object : TypeToken<T>() {}.type)
}
