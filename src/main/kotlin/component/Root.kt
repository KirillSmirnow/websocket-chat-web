package component

import csstype.Display
import csstype.LineStyle
import csstype.LineWidth
import csstype.pct
import emotion.react.css
import kotlinx.datetime.LocalDateTime
import model.chat.Message
import model.user.Session
import model.user.User
import react.FC
import react.dom.html.ReactHTML.div
import react.useState

val Root = FC<Nothing> {
    var session: Session? by useState(Session("6b67f5ec-5603-4d8d-81a1-0b5bda25334f"))

    if (session != null) {
        div {
            Chats {
                this.session = session!!
            }
            css {
                width = 20.pct
                borderStyle = LineStyle.solid
                borderWidth = LineWidth.thin
                display = Display.inlineBlock
            }
        }
        div {
            Messages {
                this.messages = listOf(
                    Message("123", User(2, "", "Max"), LocalDateTime(2022, 1, 1, 10, 0), "Hello!"),
                )
            }
            css {
                width = 50.pct
                borderStyle = LineStyle.solid
                borderWidth = LineWidth.thin
                display = Display.inlineBlock
            }
        }
        div {
            Profile {
                this.session = session!!
                this.onSignOut = { session = null }
            }
            css {
                width = 20.pct
                borderStyle = LineStyle.solid
                borderWidth = LineWidth.thin
                display = Display.inlineBlock
            }
        }
    } else {
        div {
            SignIn {
                this.onSignIn = { newSession -> session = newSession }
            }
        }
    }
}
