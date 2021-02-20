package cz.cvut.fel.dom.acoustic_event_detector.data.model

import com.fasterxml.jackson.annotation.JsonProperty
import cz.cvut.fel.dom.acoustic_event_detector.utils.Const
import cz.cvut.fel.dom.acoustic_event_detector.utils.NumberHelper.Companion.convertDouble


data class EventSensor(
    @JsonProperty("lat")
    val latitude: Double,
    @JsonProperty("lon")
    val longitude: Double,
) {
    constructor(data: Map<String, Any>) :
            this(convertDouble(data[Const.latitudeField]), convertDouble(data[Const.longitudeField]))

}


data class Event(
    @JsonProperty("latitude")
    val latitude: Double,
    @JsonProperty("longitude")
    val longitude: Double,
    @JsonProperty("id")
    val id: String?,
    @JsonProperty("timestamp")
    val timestamp: String?
) {
    constructor(data: Map<String, Any>) :
            this(
                convertDouble(data[Const.centerLatitudeField]),
                convertDouble(data[Const.centerLongitudeField]),
                data[Const.idField] as String,
                data[Const.timestampField] as String
            )


    constructor(latitude: Double, longitude: Double, timestamp: String) :
            this(latitude, longitude, null, timestamp)

    constructor(latitude: Double, longitude: Double) : this(latitude, longitude, null, null)


}

data class EventWrapper(
    @JsonProperty("event")
    val eventInfo: Event,
    @JsonProperty("sensors")
    val sensors: List<EventSensor>,
)







