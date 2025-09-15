package br.com.davijafcm.notificacoespush.repositories

import br.com.davijafcm.notificacoespush.models.Event
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : CrudRepository<Event, String> {

    fun findByUsersEmail(email: String): List<Event>

}
