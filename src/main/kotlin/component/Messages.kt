package component

import client.*
import csstype.*
import emotion.react.css
import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import model.chat.Chat
import model.chat.Message
import model.user.Session
import model.user.User
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import org.w3c.notifications.Notification
import org.w3c.notifications.NotificationOptions
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
    var webSocket: WebSocket
}

private const val messageInputId = "Messages.messageInput"
private const val messagesContainerId = "Messages.messagesContainer"

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
        Notification.requestPermission()
        props.webSocket.subscribe(props.session) { message ->
            initializedChat = null
            Notification(
                "${message.chat.name} / ${message.sender.name}",
                options = NotificationOptions(body = message.text)
            )
        }
        scrollDown()
    }

    div {
        div {
            +(selectedChat?.name ?: "Select Chat")
            css {
                fontWeight = FontWeight.bold
                height = 20.px
                minHeight = 20.px
                padding = 10.px
                whiteSpace = WhiteSpace.nowrap
                overflow = Overflow.hidden
            }
        }
        if (selectedChat != null) {
            div {
                button {
                    +(if (showInvitationCode) "Hide Invitation Code" else "Show Invitation Code")
                    onClick = { showInvitationCode = !showInvitationCode }
                }
                if (showInvitationCode) {
                    div {
                        +selectedChat.invitationCode
                        css {
                            marginLeft = 10.px
                        }
                    }
                }
                css {
                    height = 20.px
                    minHeight = 20.px
                    padding = 10.px
                    paddingTop = 0.px
                    display = Display.flex
                    borderWidth = LineWidth.thin
                    borderBottomStyle = LineStyle.solid
                }
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
                id = messagesContainerId
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
            div {
                input {
                    id = messageInputId
                    onKeyDown = { event ->
                        if (event.key == "Enter") {
                            sendMessage(props)
                        }
                    }
                    css {
                        flexGrow = number(1.0)
                    }
                }
                button {
                    +"|>"
                    onClick = { sendMessage(props) }
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

fun sendMessage(props: MessagesProps) {
    GlobalScope.launch {
        val messageInput = document.getElementById(messageInputId) as HTMLInputElement
        if (messageInput.value.isBlank()) return@launch
        sendMessage(props.session, props.chat!!, MessageSending(messageInput.value))
        messageInput.value = ""
        messageInput.focus()
        messageInput.select()
    }
}

fun scrollDown() {
    val messagesContainer = document.getElementById(messagesContainerId) as? HTMLDivElement
    messagesContainer?.scrollTo(0.0, messagesContainer.scrollHeight.toDouble())
}
