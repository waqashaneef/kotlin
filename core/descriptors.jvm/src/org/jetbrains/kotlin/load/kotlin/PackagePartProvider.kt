/*
 * Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.load.kotlin

interface PackagePartProvider {
    /**
     * @return JVM internal names of package parts existing in the package with the given FQ name, along with the [ModuleMapping] objects
     * referring to the module where each package part is defined in.
     *
     * For example, if a file named foo.kt in package org.test is compiled to a library named "mod", PackagePartProvider for such library
     * must return the list `[Pair(ModuleMapping-for-mod, "org/test/FooKt")]` for the query `"org.test"`
     * (in case the file is not annotated with @JvmName, @JvmPackageName or @JvmMultifileClass).
     */
    fun findPackageParts(packageFqName: String): List<Pair<ModuleMapping, String>>

    object Empty : PackagePartProvider {
        override fun findPackageParts(packageFqName: String): List<Pair<ModuleMapping, String>> = emptyList()
    }
}
