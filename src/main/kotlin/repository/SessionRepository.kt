package repository

import kotlinx.browser.localStorage
import model.user.Session

class SessionRepository {
    companion object {
        private const val KEY = "AccessToken"

        fun get(): Session? {
            val token = localStorage.getItem(KEY)
            return if (token == null) null else Session(token)
        }

        fun save(session: Session) {
            localStorage.setItem(KEY, session.id)
        }

        fun clear() {
            localStorage.removeItem(KEY)
        }
    }
}
