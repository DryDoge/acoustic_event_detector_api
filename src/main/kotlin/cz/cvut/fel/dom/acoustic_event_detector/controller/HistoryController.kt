package cz.cvut.fel.dom.acoustic_event_detector.controller

import cz.cvut.fel.dom.acoustic_event_detector.data.model.*
import cz.cvut.fel.dom.acoustic_event_detector.data.repository.EventsRepository
import cz.cvut.fel.dom.acoustic_event_detector.data.repository.HistoryRepository
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HistoryController(val historyRepository: HistoryRepository) {

    @PostMapping("/testHistory")
    fun postTestHistoryEvent(): Map<String, String> {
//        historyRepository.add(
//            EventWrapper(
//                Event(50.01, 14.002, "test-id"),
//
//                sensors = listOf(
//                    EventSensor(50.005, 14.0025),
//                    EventSensor(51.01, 14.003)
//                )
//            )
//        )

        historyRepository.add(
            EventWrapper(
                Event(50.00486, 14.002504),

                sensors = listOf(
                    EventSensor(50.0, 14.002504),
                    EventSensor(50.01, 14.0025),
                    EventSensor(50.0, 14.00)
                )
            )
        )
        return mapOf(Pair("Message", "Ok"))
    }

    @GetMapping("/testHistory")
    fun getTestHistoryEvent(): EventWrapper {
        return historyRepository.getById("test-id")
    }

    @DeleteMapping("/testHistory")
    fun deleteTestHistoryEvent(): Map<String, String> {
        historyRepository.delete("test-id")
        return mapOf(Pair("Message", "Deleted"))
    }

    @GetMapping("/testHistoryAll")
    fun getAllHistoryEvents(): List<EventWrapper> {
        return historyRepository.getAll()
    }
}
