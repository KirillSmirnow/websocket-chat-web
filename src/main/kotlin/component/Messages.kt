package component

import client.MessageSending
import client.getChatMessages
import client.getCurrentUser
import client.sendMessage
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
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.input
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

    div {
        +(props.chat?.name ?: "Select Chat")
        css {
            height = 20.px
            padding = 10.px
            borderWidth = LineWidth.thin
            borderBottomStyle = LineStyle.solid
            fontWeight = FontWeight.bold
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
                height = 50.pct
                padding = 4.px
                overflowY = Overflow.scroll
            }
        }
    }
    val chat = props.chat
    if (chat != null) {
        val messageSending = MessageSending(null)
        div {
            div {
                input {
                    onChange = { event -> messageSending.text = event.target.value }
                    css {
                        width = 100.pct
                    }
                }
                css {
                    width = 420.px
                    display = Display.inlineBlock
                    paddingRight = 10.px
                }
            }
            div {
                button {
                    +"|>"
                    onClick = {
                        GlobalScope.launch {
                            sendMessage(props.session, chat, messageSending)
                        }
                    }
                }
                css {
                    width = 30.px
                    display = Display.inlineBlock
                    paddingLeft = 10.px
                }
            }
            css {
                padding = 5.px
                textAlign = TextAlign.center
            }
        }
    }
}
