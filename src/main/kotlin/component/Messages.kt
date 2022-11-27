package component

import client.getChatMessages
import client.getCurrentUser
import csstype.*
import emotion.react.css
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import model.chat.Chat
import model.chat.Message
import model.user.Session
import model.user.User
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.img
import react.useState

external interface MessagesProps : Props {
    var session: Session
    var chat: Chat?
}

val Messages = FC<MessagesProps> { props ->
    var initializedChat: Chat? by useState(null)
    var messages: List<Message> by useState(emptyList())
    var user: User? by useState(null)
    GlobalScope.launch {
        val chat = props.chat
        if (chat != null && chat != initializedChat) {
            messages = getChatMessages(props.session, chat).content.reversed()
            initializedChat = chat
        }
    }
    GlobalScope.launch {
        if (user == null) {
            user = getCurrentUser(props.session)
        }
    }

    if (messages.isEmpty()) {
        div {
            img {
                src = "duck.png"
                width = 300.0
            }
            css {
                textAlign = TextAlign.center
            }
        }
    } else {
        div {
            for (message in messages) {
                div {
                    if (message.sender != user) {
                        div {
                            +message.sender.name
                            css {
                                fontStyle = FontStyle.oblique
                                fontSize = 11.pt
                            }
                        }
                    }
                    +message.text
                    css {
                        width = Length.fitContent
                        margin = 4.px
                        padding = 4.px
                        borderStyle = LineStyle.groove
                        borderWidth = LineWidth.thin
                        borderRadius = 10.px
                        if (message.sender == user) {
                            backgroundColor = Color("LemonChiffon")
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
}
