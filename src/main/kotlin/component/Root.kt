package component

import client.WebSocket
import csstype.*
import emotion.react.css
import model.chat.Chat
import model.user.Session
import react.FC
import react.dom.html.ReactHTML.div
import react.useState
import repository.SessionRepository

val Root = FC<Nothing> {
    var session: Session? by useState(SessionRepository.get())
    var chat: Chat? by useState(null)
    val webSocket: WebSocket by useState(WebSocket())

    if (session != null) {
        div {
            Chats {
                this.session = session!!
                this.onChatSelected = { newChat -> chat = newChat }
            }
            css {
                width = 25.pct
                height = 100.pct
                float = Float.left
                borderLeftStyle = LineStyle.solid
                borderWidth = LineWidth.thin
            }
        }
        div {
            Messages {
                this.session = session!!
                this.chat = chat
                this.webSocket = webSocket
            }
            css {
                width = 49.pct
                height = 100.pct
                float = Float.left
                borderLeftStyle = LineStyle.solid
                borderRightStyle = LineStyle.solid
                borderWidth = LineWidth.thin
            }
        }
        div {
            div {
                Profile {
                    this.session = session!!
                    this.onSignOut = {
                        SessionRepository.clear()
                        webSocket.unsubscribe()
                        session = null
                    }
                }
                css {
                    height = 150.px
                    minHeight = 150.px
                }
            }
            div {
                ChatMembers {
                    this.session = session!!
                    this.chat = chat
                }
                css {
                    flexGrow = number(1.0)
                    overflowY = Overflow.scroll
                    overflowX = Overflow.hidden
                    borderWidth = LineWidth.thin
                    borderTopStyle = LineStyle.solid
                }
            }
            css {
                width = 25.pct
                height = 100.pct
                float = Float.left
                display = Display.flex
                flexDirection = FlexDirection.column
                borderRightStyle = LineStyle.solid
                borderWidth = LineWidth.thin
            }
        }
    } else {
        div {
            SignIn {
                this.onSignIn = { newSession ->
                    SessionRepository.save(newSession)
                    session = newSession
                }
            }
            css {
                width = 200.px
                margin = Auto.auto
            }
        }
    }
}
