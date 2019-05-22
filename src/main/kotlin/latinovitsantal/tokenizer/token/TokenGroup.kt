package latinovitsantal.tokenizer.token

import latinovitsantal.tokenizer.*

open class TokenGroup : Token() {
    override fun toString(depth: Int): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun equals(other: Any?) = asType<TokenGroup>(other)?.let {
        it::class == this::class && it.tokens == this.tokens
    } ?: false

    val tokens = mutableListOf<Token>()

    protected open fun handleEnclosing(enclosingBrace: Char, position: Int) {
        error("wrong type of enclosing brace at $position")
    }
    protected open fun handleSuddenlyEnd() = Unit
    protected open fun handleEndOnStart() = Unit

    fun readFrom(reader: TokenReader) {
        if (!reader.hasNext) {
            handleEndOnStart()
            return
        }
        var isEnded = false
        while (!isEnded) {
            if (!reader.hasNext) {
                handleSuddenlyEnd()
                return
            }
            val char = reader.next()
            when (char) {
                in digits -> tokens += Number(char, reader)
                in javaIdParts -> tokens += Word(char, reader)
                '\"' -> tokens += Text(reader)
                '(' -> tokens += Round().apply { readFrom(reader) }
                '{' -> tokens += Curly().apply { readFrom(reader) }
                '[' -> tokens += Square().apply { readFrom(reader) }
                in "}])" -> {
                    isEnded = true
                    handleEnclosing(char, reader.position)
                }
                in reader.specials -> tokens += reader.readSymbol(char)
                !in whiteSpaces -> error("invalid character at ${reader.position}")
            }
        }
    }

    override val representation get() = "<root>"

    companion object {

        fun from(string: String, specials: List<Symbol>) =
            TokenGroup().apply { readFrom(TokenReader(string, specials)) }

    }

}