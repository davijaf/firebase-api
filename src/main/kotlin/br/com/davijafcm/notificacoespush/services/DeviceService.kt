package br.com.davijafcm.notificacoespush.services

import br.com.davijafcm.notificacoespush.models.Device
import br.com.davijafcm.notificacoespush.repositories.DeviceRepository
import org.springframework.stereotype.Service

@Service
class DeviceService(
        private val repository: DeviceRepository
) {

    fun save(device: Device): Device =
            repository.save(device)

    fun findAll(): Iterable<Device> = repository.findAll()

    fun delete(id: String) {
        repository.deleteById(id)
    }

}
