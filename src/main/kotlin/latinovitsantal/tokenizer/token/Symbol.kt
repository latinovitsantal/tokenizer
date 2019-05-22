package latinovitsantal.tokenizer.token

open class Symbol(val string: String) : Token() {
    override fun toString(depth: Int): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun equals(other: Any?) = this === other

    override val representation get() = this::class.java.simpleName

}

object Dot: Symbol(".")
object Comma: Symbol(",")
object Semicolon: Symbol(";")
object Colon: Symbol(":")
object Equal: Symbol("=")
object DoubleEqual: Symbol("==")
object TripleEqual: Symbol("===")
object And: Symbol("&")
object DoubleAnd: Symbol("&&")
object Or: Symbol("|")
object DoubleOr: Symbol("||")
object HashMark: Symbol("#")
object Asterisk: Symbol("*")
object Plus: Symbol("+")
object DoublePlus: Symbol("++")
object Minus: Symbol("-")
object DoubleMinus: Symbol("--")
object Space: Symbol(" ")
object Tab: Symbol("\t")
object NewLine: Symbol("\n")
object Lower: Symbol("<")
object Bigger: Symbol(">")
