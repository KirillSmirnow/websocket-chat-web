package component

import client.getCurrentUserChats
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import model.chat.Chat
import model.user.Session
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.useState

external interface ChatsProps : Props {
    var session: Session
}

val Chats = FC<ChatsProps> { props ->
    var chats: List<Chat> by useState(emptyList())
    GlobalScope.launch {
        chats = getCurrentUserChats(props.session).content
    }

    for (chat in chats) {
        div {
            +chat.name
        }
    }
}
