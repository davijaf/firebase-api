package br.com.davijafcm.notificacoespush.webapp.controllers

import br.com.davijafcm.notificacoespush.dto.DeviceRequest
import br.com.davijafcm.notificacoespush.services.DeviceService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import br.com.davijafcm.notificacoespush.models.Device
import org.springframework.http.HttpStatus

@Controller
@RequestMapping("devices")
class DeviceController(
        private val service: DeviceService
) {

    @GetMapping
    fun findAll() = ModelAndView("device/list").apply {
        addObject("devices", service.findAll())
    }

    @PostMapping
    fun save(@RequestBody request: Device): ResponseEntity<Void> {
        service.save(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @DeleteMapping("delete/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }

}