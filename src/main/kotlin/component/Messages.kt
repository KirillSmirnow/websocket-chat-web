package component

import model.chat.Chat
import model.user.Session
import react.FC
import react.Props

external interface MessagesProps : Props {
    var session: Session
    var chat: Chat?
}

val Messages = FC<MessagesProps> { props ->
    if (props.chat != null) {
        +"..."
    } else {
        +"Select Chat"
    }
}
