package latinovitsantal.tokenizer.token

sealed class BracedGroup : TokenGroup() {

    abstract val openingBrace: Char
    abstract val enclosingBrace: Char

    override fun handleEnclosing(enclosingBrace: Char, position: Int) {
        if (enclosingBrace != this.enclosingBrace)
            error("wrong enclosing brace at $position")
    }
    override fun handleSuddenlyEnd() = error("end instead of group")
    override fun handleEndOnStart() = error("end instead of group")

    override val representation get() = "$openingBrace$enclosingBrace"
}

class Round : BracedGroup() {
    override val openingBrace get() = '('
    override val enclosingBrace get() = ')'
}

class Square : BracedGroup() {
    override val openingBrace get() = '['
    override val enclosingBrace get() = ']'
}

class Curly : BracedGroup() {
    override val openingBrace get() = '{'
    override val enclosingBrace get() = '}'
}