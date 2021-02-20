package cz.cvut.fel.dom.acoustic_event_detector.data.repository

import cz.cvut.fel.dom.acoustic_event_detector.data.model.EventWrapper
import cz.cvut.fel.dom.acoustic_event_detector.utils.Const

class HistoryRepository : FirestoreRepository() {
    fun add(event: EventWrapper) {
        super.add(event, Const.historyCollection)
    }

    fun delete(eventId: String) {
        super.delete(eventId, Const.historyCollection)
    }

    fun getAll(): List<EventWrapper> {
        return super.getAll(Const.historyCollection)
    }

    fun getById(id: String): EventWrapper {
        return super.getById(id, Const.historyCollection)
    }
}