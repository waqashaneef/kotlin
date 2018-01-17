package org.test

@Deprecated("BinaryError", level = DeprecationLevel.ERROR)
@Experimental(ExperimentalLevel.ERROR, ExperimentalScope.BINARY)
annotation class BinaryError

@Deprecated("BinaryHidden", level = DeprecationLevel.HIDDEN)
@Experimental(ExperimentalLevel.ERROR, ExperimentalScope.BINARY)
annotation class BinaryHidden

@Deprecated("SourceError", level = DeprecationLevel.ERROR)
@Experimental(ExperimentalLevel.ERROR, ExperimentalScope.SOURCE_ONLY)
annotation class SourceError

@Deprecated("SourceHidden", level = DeprecationLevel.HIDDEN)
@Experimental(ExperimentalLevel.ERROR, ExperimentalScope.SOURCE_ONLY)
annotation class SourceHidden
