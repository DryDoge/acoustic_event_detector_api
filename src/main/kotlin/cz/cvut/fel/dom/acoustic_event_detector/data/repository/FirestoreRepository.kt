package cz.cvut.fel.dom.acoustic_event_detector.data.repository

import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import cz.cvut.fel.dom.acoustic_event_detector.data.model.Event
import cz.cvut.fel.dom.acoustic_event_detector.data.model.EventSensor
import cz.cvut.fel.dom.acoustic_event_detector.data.model.EventWrapper
import cz.cvut.fel.dom.acoustic_event_detector.utils.Const
import cz.cvut.fel.dom.acoustic_event_detector.utils.StringHelper
import java.text.NumberFormat
import java.time.LocalDateTime


open class FirestoreRepository {

    private val db: Firestore = FirestoreClient.getFirestore()

    open fun add(event: EventWrapper, collection: String) {
        try {
            val docRef =
                if (event.eventInfo.id != null && event.eventInfo.id.isNotBlank()) db.collection(collection)
                    .document(event.eventInfo.id)
                else db.collection(collection).document()

            docRef.set(prepareEventData(event.eventInfo, docRef.id, event.sensors.size))
            val sensors = prepareEventSensorsData(event.sensors)
            sensors.forEach {
                docRef.collection(Const.sensorsCollection).document().set(it)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    open fun delete(eventId: String, collection: String) {
        try {
            val docRef = db.collection(collection).document(eventId)
            val future = docRef.collection(Const.sensorsCollection).get()
            future.get().documents.forEach { it.reference.delete() }
            docRef.delete()
        } catch (e: Exception) {
            throw Exception("Error while deleting Event: $eventId")
        }
    }

    open fun getAll(collection: String): List<EventWrapper> {
        try {
            val future = db.collection(collection).get()
            val events = future.get().documents.map {
                println(it.data[Const.idField] as String)
                Event(it.data)
            }
            return events.map { event ->
                if (event.id != null) {
                    val futureSensors =
                        db.collection(collection).document(event.id)
                            .collection(Const.sensorsCollection)
                            .get()
                    val sensors = futureSensors.get().documents.map { EventSensor(it.data) }
                    EventWrapper(event, sensors)
                } else {

                    throw Exception("Event has no ID")
                }
            }
        } catch (e: Exception) {
            throw e

        }
    }

    open fun getById(id: String, collection: String): EventWrapper {
        try {
            val future = db.collection(collection).document(id).get()
            val event = Event(future.get().data as Map<String, Any>)
            val futureSensors =
                db.collection(collection).document(id).collection(Const.sensorsCollection)
                    .get()
            val sensors = futureSensors.get().documents.map { EventSensor(it.data) }
            return EventWrapper(event, sensors)
        } catch (e: Exception) {
            throw Exception("Error while getting events")
        }
    }


    private fun prepareEventData(event: Event, id: String, count: Int): Map<String, Any> {
        return mapOf(
            Pair(Const.centerLatitudeField, event.latitude),
            Pair(Const.centerLongitudeField, event.longitude),
            Pair(Const.idField, id),
            Pair(Const.timestampField, event.timestamp ?: StringHelper.formatDate(LocalDateTime.now())),
            Pair(Const.sensorsCount, count)
        )
    }

    private fun prepareEventSensorsData(sensors: List<EventSensor>): List<Map<String, Any>> {
        return sensors.map {
            mapOf(
                Pair(Const.latitudeField, it.latitude),
                Pair(Const.longitudeField, it.longitude)
            )
        }

    }


}
