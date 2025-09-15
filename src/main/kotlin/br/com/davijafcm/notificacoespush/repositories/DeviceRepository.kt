package br.com.davijafcm.notificacoespush.repositories

import br.com.davijafcm.notificacoespush.models.Device
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DeviceRepository : CrudRepository<Device, String> {

    @Query("SELECT d.firebaseToken FROM Device d")
    fun findAllTokens(): List<String>

}