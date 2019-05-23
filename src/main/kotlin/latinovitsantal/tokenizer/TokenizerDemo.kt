package latinovitsantal.tokenizer

import javafx.event.EventHandler
import javafx.geometry.Orientation.HORIZONTAL
import javafx.geometry.Orientation.VERTICAL
import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.control.TreeItem
import javafx.scene.layout.Priority.ALWAYS
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
    lateinit var showsClassCheckbox: CheckBox
    lateinit var rootTreeItem: TreeItem<Token>
    var tokenGroup = TokenGroup()

    override val root = splitpane(VERTICAL) {
        splitpane(HORIZONTAL) {
            stackpane {
                textArea = textarea(demoText) {
                    textProperty().addListener { _,_,_ -> refreshTokenGroup() }
                }
            }
            vbox {
                hbox {
                    vboxConstraints { marginTopBottom(2.0) }
                    button("Expand all") {
                        hboxConstraints { marginLeftRight(2.0) }
                        onMouseClicked = EventHandler { rootTreeItem.setIsExpandedAll(true) }
                    }
                    button("Collapse all") {
                        hboxConstraints { marginRight = 2.0 }
                        onMouseClicked = EventHandler { rootTreeItem.setIsExpandedAll(false) }
                    }
                    showsClassCheckbox = checkbox("Show classnames") {
                        selectedProperty().addListener { _,_, isSelected ->
                            refreshTree(isSelected)
                        }
                    }
                }
                treePane = stackpane {
                    vgrow = ALWAYS
                }
            }
        }
        errorLabel = label()
    }

    private fun refreshTokenGroup() {
        try {
            val newTokenGroup = TokenGroup.from(textArea.text, specials)
            if (newTokenGroup == tokenGroup)
                errorLabel.text = "no token updated"
            else {
                errorLabel.text = "tokens updated"
                tokenGroup = newTokenGroup
                refreshTree(showsClassCheckbox.isSelected)
            }
        } catch (e: Exception) {
            errorLabel.text = e.message
        }
    }

    private fun refreshTree(showsClassnames: Boolean) {
        treePane.run {
            clear()
            rootTreeItem =  TreeItem(tokenGroup)
            treeview(rootTreeItem) {
                cellFormat {
                    text = it.representation
                    if (showsClassnames) {
                        val clazz = when (it) {
                            is Symbol -> Symbol::class.java
                            is TokenGroup -> TokenGroup::class.java
                            else -> it::class.java
                        }
                        text += " (${clazz.simpleName})"
                    }
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
                root.setIsExpandedAll(true)
            }
        }
    }

    private fun <T> TreeItem<T>.setIsExpandedAll(isExpanded: Boolean) {
        this.isExpanded = isExpanded
        children.forEach { it.setIsExpandedAll(isExpanded) }
    }

    init {
        refreshTree(showsClassCheckbox.isSelected)
        refreshTokenGroup()
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
