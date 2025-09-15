package br.com.davijafcm.notificacoespush.dto

import br.com.davijafcm.notificacoespush.models.User

class UserRequest(
        val email: String,
        val user: User = User(email)
)