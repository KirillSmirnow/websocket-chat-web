package component

import client.getCurrentUserChats
import csstype.*
import emotion.react.css
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import model.chat.Chat
import model.user.Session
import react.FC
import react.Props
import react.dom.html.ReactHTML.b
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.hr
import react.useState

external interface ChatsProps : Props {
    var session: Session
    var onChatSelected: (Chat) -> Unit
}

val Chats = FC<ChatsProps> { props ->
    var initialized: Boolean by useState(false)
    var chats: List<Chat> by useState(emptyList())
    GlobalScope.launch {
        if (!initialized) {
            chats = getCurrentUserChats(props.session).content
            initialized = true
        }
    }

    div {
        div {
            b {
                +"Chats"
            }
            button {
                +"+"
                css {
                    display = Display.inlineBlock
                    float = Float.right
                }
            }
        }
        hr()

        for (chat in chats) {
            div {
                +chat.name
                onClick = {
                    chats = chats.onEach { it.selected = it == chat }
                    props.onChatSelected(chat)
                }
                css {
                    borderBottomStyle = LineStyle.solid
                    borderWidth = LineWidth.thin
                    padding = 4.px
                    hover {
                        backgroundColor = Color("Gray")
                    }
                    if (chat.selected) {
                        backgroundColor = Color("LightGray")
                    }
                }
            }
        }
        css {
            height = 98.pct
            padding = 4.px
            overflowY = Overflow.scroll
        }
    }
}
