import component.Root
import kotlinx.browser.document
import react.create
import react.dom.client.createRoot

fun main() {
    val root = document.getElementById("root")!!
    createRoot(root).render(Root.create())
}
