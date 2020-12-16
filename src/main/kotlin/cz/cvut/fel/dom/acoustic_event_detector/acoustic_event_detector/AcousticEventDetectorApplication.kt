package cz.cvut.fel.dom.acoustic_event_detector.acoustic_event_detector

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class AcousticEventDetectorApplication

fun main(args: Array<String>) {
    runApplication<AcousticEventDetectorApplication>(*args)
}
