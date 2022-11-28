package component

import client.PasswordSignIn
import client.UserRegistration
import client.registerUser
import client.startSession
import csstype.*
import emotion.react.css
import kotlinx.browser.window
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
import react.useState

external interface SignInProps : Props {
    var onSignIn: (Session) -> Unit
}

private enum class Mode { SIGN_IN, SIGN_UP }

val SignIn = FC<SignInProps> { props ->
    var mode: Mode by useState(Mode.SIGN_IN)
    when (mode) {
        Mode.SIGN_IN -> {
            val passwordSignIn = PasswordSignIn(null, null)
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
            div {
                +"Sign Up"
                onClick = { mode = Mode.SIGN_UP }
                css {
                    marginTop = 10.px
                    textAlign = TextAlign.center
                    textDecoration = TextDecoration.underline
                    hover {
                        color = Color("Green")
                        cursor = Cursor.pointer
                    }
                }
            }
        }
        Mode.SIGN_UP -> {
            val userRegistration = UserRegistration(null, null, null, null)
            div {
                label {
                    +"Nickname"
                }
            }
            div {
                input {
                    onChange = { event -> userRegistration.nickname = event.target.value }
                    css {
                        width = 100.pct
                    }
                }
            }
            div {
                label {
                    +"Name"
                }
            }
            div {
                input {
                    onChange = { event -> userRegistration.name = event.target.value }
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
                    onChange = { event -> userRegistration.password = event.target.value }
                    css {
                        width = 100.pct
                    }
                }
            }
            div {
                label {
                    +"Confirm Password"
                }
            }
            div {
                input {
                    type = InputType.password
                    onChange = { event -> userRegistration.passwordConfirmation = event.target.value }
                    css {
                        width = 100.pct
                    }
                }
            }
            div {
                button {
                    +"Sign Up"
                    onClick = { _ ->
                        GlobalScope.launch {
                            if (userRegistration.isPasswordConfirmed()) {
                                val session = registerUser(userRegistration)
                                props.onSignIn(session)
                            } else {
                                window.alert("Passwords are different")
                            }
                        }
                    }
                }
                css {
                    marginTop = 10.px
                    textAlign = TextAlign.center
                }
            }
            div {
                +"Sign In"
                onClick = { mode = Mode.SIGN_IN }
                css {
                    marginTop = 10.px
                    textAlign = TextAlign.center
                    textDecoration = TextDecoration.underline
                    hover {
                        color = Color("Green")
                        cursor = Cursor.pointer
                    }
                }
            }
        }
    }
}
