package component

import client.getCurrentUser
import csstype.Display
import csstype.Float
import csstype.px
import emotion.react.css
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import model.user.Session
import model.user.User
import react.FC
import react.Props
import react.dom.html.ReactHTML.b
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.hr
import react.useState

external interface ProfileProps : Props {
    var session: Session
    var onSignOut: () -> Unit
}

val Profile = FC<ProfileProps> { props ->
    var user: User? by useState(null)
    GlobalScope.launch {
        if (user == null) {
            user = getCurrentUser(props.session)
        }
    }

    div {
        div {
            b {
                +"Profile"
            }
            button {
                +"Sign Out"
                onClick = { _ -> props.onSignOut() }
                css {
                    display = Display.inlineBlock
                    float = Float.right
                }
            }
        }
        hr()
        div {
            +(user?.nickname ?: "...")
        }
        div {
            +(user?.name ?: "...")
        }
        css {
            padding = 4.px
        }
    }
}
