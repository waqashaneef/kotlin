/*
 * Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.load.kotlin

interface PackagePartProvider {
    /**
     * @return JVM internal names of package parts existing in the package with the given FQ name.
     *
     * For example, if a file named foo.kt in package org.test is compiled to a library, PackagePartProvider for such library
     * must return the list `["org/test/FooKt"]` for the query `"org.test"`
     * (in case the file is not annotated with @JvmName, @JvmPackageName or @JvmMultifileClass).
     */
    fun findPackageParts(packageFqName: String): List<String>

    object Empty : PackagePartProvider {
        override fun findPackageParts(packageFqName: String): List<String> = emptyList()
    }
}
