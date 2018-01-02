package org.test

class Outer {
    @Experimental(ExperimentalLevel.ERROR, ExperimentalScope.BINARY)
    annotation class Nested
}

@Outer.Nested
fun foo() {}
