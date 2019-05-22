package latinovitsantal.tokenizer

import latinovitsantal.tokenizer.token.*

fun tokens(build: TokenGroup.() -> Unit = {}) = TokenGroup().apply(build)
fun TokenGroup.text(string: String) = tokens.add(Text(string))
fun TokenGroup.word(name: String) = tokens.add(Word(name))
fun TokenGroup.special(string: String) = tokens.add(Symbol(string))
fun TokenGroup.round(build: TokenGroup.() -> Unit = {}) = tokens.add(Round().apply(build))
fun TokenGroup.curly(build: TokenGroup.() -> Unit = {}) = tokens.add(Curly().apply(build))
fun TokenGroup.square(build: TokenGroup.() -> Unit = {}) = tokens.add(Square().apply(build))

fun main() {
    val psvm = tokens {
        word("class")
        word("MyClass")
        curly {
            word("public")
            word("static")
            word("void")
            word("main")
            round {
                word("String")
                square()
                word("args")
            }
            curly {
                word("System")
                special(".")
                word("out")
                special(".")
                word("println")
                round {
                    text("Hello, World!\n")
                }
            }
        }
    }
    println(psvm)
}