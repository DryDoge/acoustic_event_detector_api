package cz.cvut.fel.dom.acoustic_event_detector.data.repository

import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import cz.cvut.fel.dom.acoustic_event_detector.data.model.Sensor

import cz.cvut.fel.dom.acoustic_event_detector.utils.Const

class SensorsRepository {

    private val db: Firestore = FirestoreClient.getFirestore()

    fun getSensorsByIds(ids: List<Long>): List<Sensor> {
        try {
            val future = db.collection(Const.sensorsCollection).get()
            return future.get().documents.map {
                Sensor(it.data)
            }.filter { ids.contains(it.id) }
        } catch (e: Exception) {
            throw e
        }
    }
}