package br.com.davijafcm.notificacoespush.models

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Device(
        @Id
        val firebaseToken: String,
        val model: String,
        val brand: String
        )