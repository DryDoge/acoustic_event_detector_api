package cz.cvut.fel.dom.acoustic_event_detector.data.repository

import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import cz.cvut.fel.dom.acoustic_event_detector.utils.Const

class UsersRepository {

    private val db: Firestore = FirestoreClient.getFirestore()

    fun add(uid: String, email: String) {
        try {
            val docRef = db.collection(Const.usersCollection).document(uid)
            docRef.set(
                mapOf(
                    Pair(Const.emailField, email),
                    Pair(Const.rightsField, 1)
                )
            )
        } catch (e: Exception) {
            throw e
        }
    }
}