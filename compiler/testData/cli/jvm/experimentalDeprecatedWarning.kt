package org.test

@Deprecated("BinaryWarning", level = DeprecationLevel.WARNING)
@Experimental(ExperimentalLevel.ERROR, ExperimentalScope.BINARY)
annotation class BinaryWarning

@Deprecated("SourceWarning", level = DeprecationLevel.WARNING)
@Experimental(ExperimentalLevel.ERROR, ExperimentalScope.SOURCE_ONLY)
annotation class SourceWarning
