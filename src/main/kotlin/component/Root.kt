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
                this.chat = null
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
            Profile {
                this.session = session!!
                this.onSignOut = {
                    SessionRepository.clear()
                    session = null
                }
            }
            css {
                width = 25.pct
                height = 100.pct
                float = Float.left
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
