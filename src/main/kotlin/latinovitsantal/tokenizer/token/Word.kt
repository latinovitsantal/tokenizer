package latinovitsantal.tokenizer.token

import latinovitsantal.tokenizer.TokenReader
import latinovitsantal.tokenizer.asType

class Word(val string: String) : Token() {
    override fun toString(depth: Int): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun equals(other: Any?) = asType<Word>(other)?.let { it.string == string } ?: false

    override val representation get() = string

    constructor(start: Char, reader: TokenReader) : this(buildString {
        append(start)
        while (reader.hasNext && reader.isNextJavaIdPart)
            append(reader.next())
    })


}