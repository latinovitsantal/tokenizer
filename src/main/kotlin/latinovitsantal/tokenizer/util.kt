package latinovitsantal.tokenizer


val javaIdStarts = CharContainmentPredicate { it.isJavaIdentifierStart() }
val javaIdParts = CharContainmentPredicate { it.isJavaIdentifierPart() }
val whiteSpaces = CharContainmentPredicate { it.isWhitespace() }
val digits = CharContainmentPredicate { it.isDigit() }

class CharContainmentPredicate(private val predicate: (Char) -> Boolean) {
    operator fun contains(char: Char) = predicate(char)
}

inline fun <reified T> asType(obj: Any?): T? = when(obj) {
    is T -> obj
    else -> null
}