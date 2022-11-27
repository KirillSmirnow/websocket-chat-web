package component

import model.chat.Message
import react.FC
import react.Props
import react.dom.html.ReactHTML.div

external interface MessagesProps : Props {
    var messages: List<Message>
}

val Messages = FC<MessagesProps> { props ->
    for (message in props.messages) {
        div {
            +message.text
        }
    }
}
