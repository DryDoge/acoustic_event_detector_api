package cz.cvut.fel.dom.acoustic_event_detector.data.repository

import cz.cvut.fel.dom.acoustic_event_detector.data.model.EventWrapper
import cz.cvut.fel.dom.acoustic_event_detector.utils.Const

class EventsRepository : FirestoreRepository() {
    fun add(event: EventWrapper) {
        super.add(event, Const.eventsCollection)
    }

    fun delete(eventId: String) {
        super.delete(eventId, Const.eventsCollection)
    }

    fun getAll(): List<EventWrapper> {
        return super.getAll(Const.eventsCollection)
    }

    fun getById(id: String): EventWrapper {
        return super.getById(id, Const.eventsCollection)
    }
}