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
    val selectedChat = props.chat
    var initializedChat: Chat? by useState(null)
    var messages: List<Message> by useState(emptyList())
    var user: User? by useState(null)
    var showInvitationCode: Boolean by useState(false)
    GlobalScope.launch {
        if (selectedChat != null && selectedChat != initializedChat) {
            messages = getChatMessages(props.session, selectedChat).content.reversed()
            initializedChat = selectedChat
        }
        if (user == null) {
            user = getCurrentUser(props.session)
        }
    }

    div {
        div {
            div {
                +(selectedChat?.name ?: "Select Chat")
                css {
                    fontWeight = FontWeight.bold
                    flexGrow = number(1.0)
                }
            }
            if (selectedChat != null) {
                if (showInvitationCode) {
                    div {
                        +selectedChat.invitationCode
                        css {
                            marginRight = 10.px
                        }
                    }
                }
                button {
                    +(if (showInvitationCode) "Hide Invitation Code" else "Show Invitation Code")
                    onClick = { showInvitationCode = !showInvitationCode }
                }
            }
            css {
                height = 20.px
                padding = 10.px
                display = Display.flex
                borderWidth = LineWidth.thin
                borderBottomStyle = LineStyle.solid
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
                    flexGrow = number(1.0)
                }
            }
        } else {
            div {
                for (message in messages) {
                    div {
                        if (message.sender == user) {
                            div {
                                css {
                                    flexGrow = number(1.0)
                                }
                            }
                        }
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
                                maxWidth = 400.px
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
                        css {
                            display = Display.flex
                        }
                    }
                }
                css {
                    padding = 4.px
                    flexGrow = number(1.0)
                    overflowY = Overflow.scroll
                }
            }
        }
        if (selectedChat != null) {
            val messageSending = MessageSending(null)
            div {
                input {
                    onChange = { event -> messageSending.text = event.target.value }
                    css {
                        flexGrow = number(1.0)
                    }
                }
                button {
                    +"|>"
                    onClick = {
                        GlobalScope.launch {
                            sendMessage(props.session, selectedChat, messageSending)
                        }
                    }
                }
                css {
                    display = Display.flex
                    padding = 10.px
                    borderWidth = LineWidth.thin
                    borderTopStyle = LineStyle.solid
                }
            }
        }
        css {
            height = 100.pct
            display = Display.flex
            flexDirection = FlexDirection.column
        }
    }
}
