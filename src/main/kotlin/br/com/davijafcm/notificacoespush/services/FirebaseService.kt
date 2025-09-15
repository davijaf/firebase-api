package br.com.davijafcm.notificacoespush.services

import br.com.davijafcm.notificacoespush.models.Event
import br.com.davijafcm.notificacoespush.models.FirebaseData
import br.com.davijafcm.notificacoespush.repositories.DeviceRepository
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.*
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.io.File

private const val FIREBASE_DIRECTORY = "./firebase/"
private const val FIREBASE_ALREADY_EXISTS = "FirebaseApp name [DEFAULT] already exists"

@Service
class FirebaseService(
        private val deviceRepository: DeviceRepository
) {

    private val tokens: List<String> get() = deviceRepository.findAllTokens()
    val connectionState get() = firebaseConnectionState

    init {
        setupFirebase()
    }

    final fun setupFirebase() {
        try {
            tryReadJsonFile()?.let { file ->
                println(file.name)
                val service = file.inputStream()
                val options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(service))
                        .build()
                FirebaseApp.initializeApp(options)
                firebaseConnectionState = true
                println("Firebase initialized successfully")
            }
        } catch (e: NoSuchElementException) {
            println("Failure to read Firebase config file")
        } catch (e: Exception) {
            firebaseConnectionState = e.message?.contains(FIREBASE_ALREADY_EXISTS) ?: false
            if (firebaseConnectionState) {
                println("Configuration already exists")
            } else {
                e.printStackTrace()
            }
        }
    }

    private fun tryReadJsonFile() = File(FIREBASE_DIRECTORY).run {
        mkdir()
        listFiles()?.first {
            it.name.endsWith(".json")
        }
    }

    @Async
    fun sendNewEvent(event: Event) =
            sendMulticastData(mapOf(
                    "titulo" to event.title,
                    "descricao" to event.description,
                    "imagem" to event.image
            ))

    @Async
    fun sendData(message: FirebaseData) {
        sendMulticastData(
                mapOf(
                        "titulo" to message.title,
                        "descricao" to message.message,
                        "image" to message.image
                ))
    }

    @Async
    fun sendNotification(message: FirebaseData) {
        sendMulticastNotification(
                message.title,
                message.message,
                message.image
        )
    }


    private fun sendMulticastNotification(
            title: String,
            body: String,
            image: String = ""
    ) {
        if (tokens.isNotEmpty()) {
            try {
                val message = createNotification(title, body, image)
                val responses = sendMulticast(message)
                val failureCount = responses.count { !it.isSuccessful }
                if (failureCount > 0) {
                    removeUnregisteredTokens(responses)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun sendMulticastData(data: Map<String, String>) {
        if (tokens.isNotEmpty()) {
            try {
                val message = createData(data)
                val responses = sendMulticast(message)
                val failureCount = responses.count { !it.isSuccessful }
                if (failureCount > 0) {
                    removeUnregisteredTokens(responses)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun createData(data: Map<String, String>) =
            MulticastMessage.builder()
                    .putAllData(data)
                    .addAllTokens(tokens)
                    .build()

    private fun removeUnregisteredTokens(responses: List<SendResponse>) {
        // Associate current tokens list with responses by index
        tokens.withIndex().forEach { (index, token) ->
            val response = responses.getOrNull(index) ?: return@forEach
            val unregistered = response.exception?.messagingErrorCode == MessagingErrorCode.UNREGISTERED
            if (!response.isSuccessful && unregistered) {
                deviceRepository.deleteById(token)
            }
        }
    }

    private fun createNotification(
            title: String,
            body: String,
            image: String = ""
    ) = MulticastMessage.builder()
            .setNotification(
                    Notification.builder()
                            .setImage(image)
                            .setTitle(title)
                            .setBody(body)
                            .build())
            .addAllTokens(tokens)
            .build()

    private fun sendMulticast(message: MulticastMessage): List<SendResponse> {
        // Use sendEachForMulticast to avoid deprecated batch endpoint (/batch 404)
        val batchResponse = FirebaseMessaging
            .getInstance()
            .sendEachForMulticast(message)
        return batchResponse.responses
    }

    @Async
    fun sendEventCancelation(event: Event) {
        val message = Message.builder()
                .putData("titulo", "Evento cancelado")
                .putData("descricao", event.title)
                .setTopic(event.id)
                .build()
        FirebaseMessaging.getInstance().send(message)
    }

    companion object {
        private var firebaseConnectionState = false
    }

}

