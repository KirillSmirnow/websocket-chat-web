package component

import csstype.*
import emotion.react.css
import model.user.Session
import react.FC
import react.dom.html.ReactHTML.div
import react.useState
import repository.SessionRepository

val Root = FC<Nothing> {
    var session: Session? by useState(SessionRepository.get())

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
                this.session = session!!
                this.chat = null
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
                this.onSignOut = {
                    SessionRepository.clear()
                    session = null
                }
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
