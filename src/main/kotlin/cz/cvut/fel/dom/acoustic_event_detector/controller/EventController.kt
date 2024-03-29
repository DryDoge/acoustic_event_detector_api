package cz.cvut.fel.dom.acoustic_event_detector.controller

import cz.cvut.fel.dom.acoustic_event_detector.data.repository.EventsRepository
import org.springframework.web.bind.annotation.*
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import cz.cvut.fel.dom.acoustic_event_detector.calculation.Calculator
import cz.cvut.fel.dom.acoustic_event_detector.data.model.*
import cz.cvut.fel.dom.acoustic_event_detector.data.repository.SensorsRepository
import cz.cvut.fel.dom.acoustic_event_detector.utils.Const


@RestController
class EventController(val eventsRepository: EventsRepository) {

    @PostMapping("/newFinalEvent")
    fun newFinalEvent(@RequestBody event: EventWrapper): Map<String, String> {
        eventsRepository.add(event)

        val notificationStatus = sendNotification()
        return if (notificationStatus) mapOf(
            Pair("Message", "Ok")
        ) else mapOf(
            Pair("Message", "Notification was not send")
        )
    }

    @PostMapping("/newEvent")
    fun newEvent(@RequestBody possibleEvent: PossibleEvent): Map<String, String> {

        val idsOfSensors = possibleEvent.report.map { it.id }
        val sensors = SensorsRepository().getSensorsByIds(idsOfSensors)

        if (sensors.size > 2) {
            val calculator = Calculator(possibleEvent, sensors)
            val center = calculator.getCenter()

            val event = EventWrapper(Event(center[0], center[1]), sensors.map { EventSensor(it.lat, it.lon) })
            eventsRepository.add(event)

            val notificationStatus = sendNotification()
            return if (notificationStatus) mapOf(
                Pair("Message", "Ok")
            ) else mapOf(
                Pair("Message", "Notification was not send")
            )
        }
        return mapOf(Pair("Message", "Not enough sensors"))
    }

    private fun sendNotification(): Boolean {
        val notification: Notification =
            Notification.builder().setTitle("New event").setBody("Hey, a new event was reported!").build()

        val message: Message = Message.builder().setNotification(notification)
            .putData("click_action", "FLUTTER_NOTIFICATION_CLICK")
            .setTopic(Const.eventsToken)
            .build()

        return try {
            FirebaseMessaging.getInstance().send(message)
            true
        } catch (error: FirebaseMessagingException) {
            false
        }
    }
}
