package cz.cvut.fel.dom.acoustic_event_detector.data.model

import com.fasterxml.jackson.annotation.JsonProperty
import cz.cvut.fel.dom.acoustic_event_detector.utils.Const
import cz.cvut.fel.dom.acoustic_event_detector.utils.NumberHelper

data class Sensor(val address: String, val dbID: String, val id: Long, val lat: Double, val lon: Double) {
    constructor(data: Map<String, Any>) :
            this(
                data[Const.addressField] as String,
                data[Const.dbIdField] as String,
                data[Const.idField] as Long,
                NumberHelper.convertDouble(data[Const.latitudeField]),
                NumberHelper.convertDouble(data[Const.longitudeField])
            )

}

data class SensorInfo(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("time")
    val time: Long
)

data class PossibleEvent(
    @JsonProperty("report")
    val report: List<SensorInfo>
)
