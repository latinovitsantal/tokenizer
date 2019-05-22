package latinovitsantal.tokenizer

import latinovitsantal.tokenizer.token.*

class TokenReader(private val string: String, symbols: List<Symbol>) {

    var i = 0; private set
    val position get() = i - 1
    fun next() = string[i++]
    val hasNext get() = i < string.length
    val isNextWhiteSpace get() = string[i].isWhitespace()
    val isNextJavaIdPart get() = string[i].isJavaIdentifierPart()
    val isNextNumberPart get() = string[i].isDigit() || string[i] == '.'
    private val next get() = string[i]

    fun skipWhitespaces() {
        while (hasNext && isNextWhiteSpace)
            i++
    }

    class SymbolPicker {
        private val nestedPickers = mutableMapOf<Char, SymbolPicker>()
        var symbol: Symbol? = null
            private set
        fun add(symbol: Symbol, index: Int = 0) {
            if (index == symbol.string.length)
                this.symbol = symbol
            else nestedPickers.getOrPut(symbol.string[index]) { SymbolPicker() }
                .add(symbol, index + 1)
        }
        fun getNestedAt(char: Char) = nestedPickers[char]
    }

    private val rootPicker = SymbolPicker().apply { symbols.forEach { add(it) } }

    val specials = CharContainmentPredicate { rootPicker.getNestedAt(it) != null }

    fun readSymbol(start: Char): Symbol {
        var nextPicker = rootPicker.getNestedAt(start)
        var actualPicker = nextPicker
        val hn = hasNext
        while (nextPicker != null && hasNext) {
            actualPicker = nextPicker
            nextPicker = actualPicker.getNestedAt(next)
            if (nextPicker != null)
                next()
        }
        return actualPicker?.symbol ?: error("invalid text at $position")
    }

}

