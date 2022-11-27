package component

import client.PasswordSignIn
import client.startSession
import csstype.Auto
import csstype.TextAlign
import csstype.pct
import csstype.px
import emotion.react.css
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import model.user.Session
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label

external interface SignInProps : Props {
    var onSignIn: (Session) -> Unit
}

val SignIn = FC<SignInProps> { props ->
    val passwordSignIn = PasswordSignIn(null, null)

    div {
        div {
            label {
                +"Nickname"
            }
        }
        div {
            input {
                onChange = { event -> passwordSignIn.nickname = event.target.value }
                css {
                    width = 100.pct
                }
            }
        }
        div {
            label {
                +"Password"
            }
        }
        div {
            input {
                type = InputType.password
                onChange = { event -> passwordSignIn.password = event.target.value }
                css {
                    width = 100.pct
                }
            }
        }
        div {
            button {
                +"Sign In"
                onClick = { _ ->
                    GlobalScope.launch {
                        val session = startSession(passwordSignIn)
                        props.onSignIn(session)
                    }
                }
            }
            css {
                marginTop = 10.px
                textAlign = TextAlign.center
            }
        }
        css {
            width = 200.px
            margin = Auto.auto
        }
    }
}
