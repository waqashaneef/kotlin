package foo

import foo.Base.Companion.FromBaseCompanion
import foo.CompanionSupertype.FromCompanionSupertype

open class Base {
    companion object {
        class FromBaseCompanion
    }
}

open class CompanionSupertype {
    class FromCompanionSupertype
}

class Derived : Base() {
    companion object : CompanionSupertype() {
    }

    val a: FromBaseCompanion? = null
    val b: FromCompanionSupertype? = null
}