package latinovitsantal.tokenizer

import javafx.geometry.Orientation.HORIZONTAL
import javafx.geometry.Orientation.VERTICAL
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.control.TreeItem
import javafx.scene.layout.StackPane
import javafx.scene.text.Font
import javafx.stage.Stage
import latinovitsantal.tokenizer.token.*
import tornadofx.*
import java.lang.Exception
import javafx.scene.paint.Color.LIGHTGREY as LIGHTGREY
import javafx.scene.text.FontWeight.BOLD as BOLD

class TokenizerDemo: App(MainView::class) {
    override fun start(stage: Stage) {
        super.start(stage)
        stage.width = 600.0
        stage.height = 600.0
    }
}
fun main(args: Array<String>) = launch<TokenizerDemo>(args)

class MainView: View() {

    lateinit var textArea: TextArea
    lateinit var treePane: StackPane
    lateinit var errorLabel: Label
    var tokenGroup: TokenGroup? = null

    override val root = splitpane(VERTICAL) {
        splitpane(HORIZONTAL) {
            stackpane {
                textArea = textarea(demoText) {
                    textProperty().addListener { _,_,_ -> tryRefreshTree() }
                }
            }
            treePane = stackpane()
        }
        errorLabel = label()
    }

    private fun tryRefreshTree() {
        try {
            val newTokenGroup = TokenGroup.from(textArea.text, specials)
            if (newTokenGroup == tokenGroup)
                errorLabel.text = "no token updated"
            else {
                errorLabel.text = "tokens updated"
                tokenGroup = newTokenGroup
                refreshTree()
            }
        } catch (e: Exception) {
            errorLabel.text = e.message
        }
    }

    private fun refreshTree() {
        treePane.run {
            clear()
            treeview(TreeItem<Token>(tokenGroup)) {
                isShowRoot = false
                cellFormat {
                    text = it.representation
                    font = Font.font("Source Code Pro", 12.0)
                    paddingAll = 0.0
                    if (it is TokenGroup) style {
                        backgroundColor += LIGHTGREY
                    }
                }
                populate {
                    val token = it.value
                    if (token is TokenGroup) token.tokens else null
                }
                expand(root)
            }
        }
    }

    private fun <T> expand(treeItem: TreeItem<T>) {
        treeItem.isExpanded = true
        treeItem.children.forEach(this::expand)
    }

    init {
        tryRefreshTree()
    }

}

val specials = listOf(Dot, Semicolon, Equal, DoubleEqual, Lower, DoublePlus)

val tab = '\t'
val demoText = """
    class Main {
    ${tab}public static void main(String[] args) {
    ${tab}${tab}System.out.println("Hello, World!");
    ${tab}${tab}for (int i = 0; i < 10; i++) {
    ${tab}${tab}${tab}System.out.println(i);
    ${tab}${tab}}
    ${tab}}
    }
""".trimIndent()
