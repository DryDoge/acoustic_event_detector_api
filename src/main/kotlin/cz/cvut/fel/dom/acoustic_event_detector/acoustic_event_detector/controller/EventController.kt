package cz.cvut.fel.dom.acoustic_event_detector.acoustic_event_detector.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class EventController {

    @PostMapping("/")
    fun add(): String {
        return "hello"
    }
}