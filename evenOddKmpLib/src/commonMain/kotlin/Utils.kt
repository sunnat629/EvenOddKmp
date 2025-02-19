@file:OptIn(ExperimentalJsExport::class)

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@JsExport
fun isEven(number: Int): Boolean {
    return number % 2 == 0
}

@JsExport
fun isOdd(number: Int): Boolean {
    return number % 2 != 0
}