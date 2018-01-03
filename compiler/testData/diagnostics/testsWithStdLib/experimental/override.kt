// MODULE: api
// FILE: api.kt

package api

@Experimental(ExperimentalLevel.WARNING, ExperimentalScope.SOURCE_ONLY)
annotation class E

open class Base {
    @E
    open fun foo() {}
}

class DerivedInSameModule : Base() {
    override fun <!EXPERIMENTAL_OVERRIDE!>foo<!>() {}
}

// MODULE: usage1(api)
// FILE: usage-propagate.kt

package usage1

import api.*

open class Derived : Base() {
    @E
    override fun foo() {}
}

class SubDerived : Derived()

@E
class Derived2 : Base() {
    override fun foo() {}
}

// MODULE: usage2(api)
// FILE: usage-none.kt

package usage2

import api.*

class Derived : Base() {
    override fun <!EXPERIMENTAL_OVERRIDE!>foo<!>() {}
}
