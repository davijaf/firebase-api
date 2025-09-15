package br.com.davijafcm.notificacoespush.dto

import br.com.davijafcm.notificacoespush.models.Device

open class DeviceRequest(
        modelo: String,
        marca: String,
        token: String,
        val device: Device = Device(
                model = modelo,
                brand = marca,
                firebaseToken = token)
) {

    override fun toString(): String {
        return "device=$device"
    }

}

class DeviceResponse(
        device: Device,
        val modelo: String = device.model,
        val marca: String = device.brand,
        val token: String = device.firebaseToken
)

