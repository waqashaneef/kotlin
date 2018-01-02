// !DIAGNOSTICS: -NOTHING_TO_INLINE
// FILE: api.kt

package api

@Experimental(ExperimentalLevel.WARNING, ExperimentalScope.SOURCE_ONLY)
annotation class ExperimentalSourceOnlyAPI

@Experimental(ExperimentalLevel.WARNING, ExperimentalScope.BINARY)
annotation class ExperimentalBinaryAPI

@ExperimentalSourceOnlyAPI
fun sourceOnly() {}

@ExperimentalBinaryAPI
fun binary() {}

// FILE: usage.kt

package usage

import api.*

fun use1() {
    sourceOnly()
    binary()
}

val use2 = sourceOnly()
val use3 = binary()


inline fun inlineUse1() {
    <!EXPERIMENTAL_API_USAGE!>sourceOnly<!>()
    <!EXPERIMENTAL_API_USAGE!>binary<!>()
}

inline var inlineUse2: Unit
    get() {
        <!EXPERIMENTAL_API_USAGE!>sourceOnly<!>()
        <!EXPERIMENTAL_API_USAGE!>binary<!>()
    }
    set(value) {
        <!EXPERIMENTAL_API_USAGE!>sourceOnly<!>()
        <!EXPERIMENTAL_API_USAGE!>binary<!>()
    }

var inlineUse3: Unit
    inline get() {
        <!EXPERIMENTAL_API_USAGE!>sourceOnly<!>()
        <!EXPERIMENTAL_API_USAGE!>binary<!>()
    }
    @ExperimentalSourceOnlyAPI
    @ExperimentalBinaryAPI
    inline set(value) {
        sourceOnly()
        binary()
    }

@ExperimentalSourceOnlyAPI
@ExperimentalBinaryAPI
inline fun inlineUse4() {
    sourceOnly()
    binary()
}
