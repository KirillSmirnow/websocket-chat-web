package component

import client.getChatMembers
import csstype.Color
import csstype.LineStyle
import csstype.LineWidth
import csstype.px
import emotion.react.css
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import model.chat.Chat
import model.user.Session
import model.user.User
import react.FC
import react.Props
import react.dom.html.ReactHTML.b
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.hr
import react.useState

external interface ChatMembersProps : Props {
    var session: Session
    var chat: Chat?
}

val ChatMembers = FC<ChatMembersProps> { props ->
    val selectedChat = props.chat
    var initializedChat: Chat? by useState(null)
    var members: List<User> by useState(emptyList())
    GlobalScope.launch {
        if (selectedChat != null && selectedChat != initializedChat) {
            members = getChatMembers(props.session, selectedChat).content
            initializedChat = selectedChat
        }
    }

    if (selectedChat == null) return@FC
    div {
        div {
            b {
                +"Chat Members (${members.size})"
            }
        }
        hr()
        div {
            for (member in members) {
                div {
                    +member.name
                    css {
                        borderBottomStyle = LineStyle.solid
                        borderWidth = LineWidth.thin
                        hover {
                            backgroundColor = Color("Gray")
                        }
                    }
                }
            }
        }
        css {
            padding = 4.px
        }
    }
}
