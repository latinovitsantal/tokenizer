package latinovitsantal.tokenizer.token

import latinovitsantal.tokenizer.TokenReader
import latinovitsantal.tokenizer.asType
import org.unbescape.json.JsonEscape.*

class Text(val string: String) : Token() {

    override fun equals(other: Any?) = asType<Text>(other)?.let { it.string == string } ?: false
    override fun toString(depth: Int): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override val representation get() = "\"${escapeJsonMinimal(string)}\""


    constructor(reader: TokenReader): this(unescapeJson(buildString {
        if (!reader.hasNext) error("end instead of text")
        var isEscapeComing = false
        var isEnded = !reader.hasNext
        while (!isEnded) {
            val char = reader.next()
            isEnded = char == '\"' && !isEscapeComing
            if (!isEnded) {
                append(char)
                if (!reader.hasNext) error("text isn't closed till the end")
            }
            isEscapeComing = !isEscapeComing && char == '\\'
        }
    }))

}