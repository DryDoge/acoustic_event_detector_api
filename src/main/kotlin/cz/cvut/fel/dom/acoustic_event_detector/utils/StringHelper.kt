package cz.cvut.fel.dom.acoustic_event_detector.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class StringHelper {
    companion object{
        fun formatDate(date: LocalDateTime): String{
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            return date.format(formatter)
        }
    }
}