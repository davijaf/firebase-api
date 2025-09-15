package br.com.davijafcm.notificacoespush

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAutoConfiguration
@EnableAspectJAutoProxy
@EnableAsync
class FCMApplication

fun main(args: Array<String>) {
    // Added: guard to give a clear message if launched with the wrong Java version
    val spec = System.getProperty("java.specification.version") // e.g. "1.8" or "11"
    val major = spec.toDoubleOrNull()
    if (major == null || major < 11.0) {
        System.err.println("This application requires Java 11+. Detected: ${System.getProperty("java.version")}. Please configure your IDE/run config to use JDK 11.")
        return
    }
	runApplication<FCMApplication>(*args)
}