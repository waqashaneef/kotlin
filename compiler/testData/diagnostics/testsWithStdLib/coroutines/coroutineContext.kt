// SKIP_TXT
// !DIAGNOSTICS: -UNUSED_PARAMETER

import kotlin.coroutines.experimental.intrinsics.coroutineContext
import kotlin.coroutines.experimental.CoroutineContext

fun ordinal() {
    <!ILLEGAL_SUSPEND_PROPERTY_ACCESS!>coroutineContext<!>
}

suspend fun named() {
    coroutineContext
}

class A {
    val coroutineContextOld = kotlin.coroutines.experimental.intrinsics.<!ILLEGAL_SUSPEND_PROPERTY_ACCESS!>coroutineContext<!>
    val coroutineContextNew = kotlin.coroutines.experimental.<!ILLEGAL_SUSPEND_PROPERTY_ACCESS!>coroutineContext<!>
}

class Controller {
    fun ordinal() {
        <!ILLEGAL_SUSPEND_PROPERTY_ACCESS!>kotlin.coroutines.experimental.intrinsics.coroutineContext<!>
        <!ILLEGAL_SUSPEND_PROPERTY_ACCESS!>kotlin.coroutines.experimental.coroutineContext<!>
    }

    suspend fun named() {
        kotlin.coroutines.experimental.intrinsics.coroutineContext
        kotlin.coroutines.experimental.coroutineContext
    }

    suspend fun severalArgs(s: String, a: Any) {
        kotlin.coroutines.experimental.intrinsics.coroutineContext
        kotlin.coroutines.experimental.coroutineContext
    }
}

fun builder(c: () -> CoroutineContext) = {}
fun builderSuspend(c: suspend () -> CoroutineContext) = {}

fun builderSeveralArgs(c: (Int, Int, Int) -> CoroutineContext) = {}
fun builderSuspendSeveralArgs(c: suspend (Int, Int, Int) -> CoroutineContext) = {}

fun test() {
    builder { <!ILLEGAL_SUSPEND_PROPERTY_ACCESS!>kotlin.coroutines.experimental.intrinsics.coroutineContext<!> }
    builder { <!ILLEGAL_SUSPEND_PROPERTY_ACCESS!>kotlin.coroutines.experimental.coroutineContext<!> }
    builderSuspend { kotlin.coroutines.experimental.intrinsics.coroutineContext }
    builderSuspend { kotlin.coroutines.experimental.coroutineContext }
    builderSeveralArgs {_, _,_ -> <!ILLEGAL_SUSPEND_PROPERTY_ACCESS!>kotlin.coroutines.experimental.intrinsics.coroutineContext<!> }
    builderSeveralArgs {_, _,_ -> <!ILLEGAL_SUSPEND_PROPERTY_ACCESS!>kotlin.coroutines.experimental.coroutineContext<!> }
    builderSuspendSeveralArgs {_, _,_ -> kotlin.coroutines.experimental.intrinsics.coroutineContext}
    builderSuspendSeveralArgs {_, _,_ -> kotlin.coroutines.experimental.coroutineContext}
}
