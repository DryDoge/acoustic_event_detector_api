package cz.cvut.fel.dom.acoustic_event_detector.utils

class NumberHelper {
    companion object {
        fun convertDouble(value: Any?): Double {
            return if (value is Long) value.toDouble() else value as Double
        }
    }
}