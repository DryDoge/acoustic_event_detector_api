package cz.cvut.fel.dom.acoustic_event_detector

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.FileInputStream
import com.google.firebase.FirebaseApp

import com.google.auth.oauth2.GoogleCredentials

import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserRecord
import cz.cvut.fel.dom.acoustic_event_detector.data.repository.EventsRepository
import cz.cvut.fel.dom.acoustic_event_detector.data.repository.UsersRepository
import org.springframework.context.annotation.Bean
import java.io.File
import kotlin.system.exitProcess


@SpringBootApplication
class AcousticEventDetectorApplication {
    @Bean
    fun eventRepository(): EventsRepository {
        return EventsRepository()
    }

}

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("First argument must be path to config file from firebase")
        exitProcess(-1)
    }
    val configPath = args[0]
    initializeFirebaseAdmin(configPath)
    val port = try {
        args[1].toInt()
    } catch (e: Exception) {
        8080
    }
    System.getProperties()["server.port"] = port
    runApplication<AcousticEventDetectorApplication>(*args)
    createAdminUser()
}

private fun initializeFirebaseAdmin(configPath: String) {
    try {
        FirebaseApp.getInstance()
    } catch (e: IllegalStateException) {
        val serviceAccount = FileInputStream(File(configPath))
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()
        FirebaseApp.initializeApp(options)

    }
}

private fun createAdminUser() {
    val request = UserRecord.CreateRequest().setEmail("admin@aed.com").setPassword("cvut123").setDisabled(false)
    try {
        val user = FirebaseAuth.getInstance().createUser(request)
        UsersRepository().add(user.uid, user.email)
        println("User ${user.email} was created.")
    } catch (e: FirebaseAuthException) {
        println("User already exists!")
    } catch (e: Exception) {
        println(e)
        exitProcess(-1)
    }
}
