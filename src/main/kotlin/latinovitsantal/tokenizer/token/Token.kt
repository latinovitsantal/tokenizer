package latinovitsantal.tokenizer.token

abstract class Token {

    abstract override fun equals(other: Any?): Boolean
    override fun toString() = toString(0)
    abstract fun toString(depth: Int): String
    abstract val representation: String

}

