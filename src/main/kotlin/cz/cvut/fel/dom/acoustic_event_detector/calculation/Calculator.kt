package cz.cvut.fel.dom.acoustic_event_detector.calculation


import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver
import cz.cvut.fel.dom.acoustic_event_detector.data.model.PossibleEvent
import cz.cvut.fel.dom.acoustic_event_detector.data.model.Sensor
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer


class Calculator(possibleEvent: PossibleEvent, sensors: List<Sensor>) {
    private val speedOfSound = 0.3436

    private var positions: Array<DoubleArray>
    private val distances: DoubleArray
    private val referencePoint: DoubleArray

    init {
        val refPoint = possibleEvent.report.minByOrNull { it.time }!!
        val refSensor = sensors.first { it.id == refPoint.id }

        val sensorsInfoFiltered = possibleEvent.report.filter { it.id != refPoint.id }.sortedBy { it.id }
        val sensorsFiltered = sensors.filter { it.id != refPoint.id }.sortedBy { it.id }

        referencePoint = doubleArrayOf(refSensor.lat, refSensor.lon)
        distances = sensorsInfoFiltered.map { (it.time - refPoint.time) * speedOfSound }.toDoubleArray()
        positions = Array(sensorsFiltered.size) { DoubleArray(2) { 0.0 } }
        for (i in sensorsFiltered.indices) {
            println(i)
            positions[i][0] = sensorsFiltered[i].lat
            positions[i][1] = sensorsFiltered[i].lon
        }


    }

    fun getCenter(): DoubleArray {
        val func = MultilaterationFunction(positions, distances, referencePoint)
        val optimizer = LevenbergMarquardtOptimizer()
        val solver = NonLinearLeastSquaresSolver(func, optimizer)

        val optimum = solver.solve()

        val centroid = optimum.point.toArray()
        val standardDeviation = optimum.getSigma(0.0).toArray()
        if (centroid[0] > 90.0 &&
            centroid[0] - standardDeviation[0] >= -90.0 &&
            centroid[0] - standardDeviation[0] <= 90.0
        ) {
            centroid[0] -= standardDeviation[0]
        } else if (centroid[0] < -90.0 &&
            centroid[0] + standardDeviation[0] >= -90.0 &&
            centroid[0] + standardDeviation[0] <= 90.0
        ) {
            centroid[0] += standardDeviation[0]
        }
        if (centroid[1] < -180.0 &&
            centroid[1] + standardDeviation[1] >= -180.0 &&
            centroid[1] + standardDeviation[1] < 180.0
        ) {
            centroid[1] += standardDeviation[1]
        } else if (centroid[1] >= 180.0 &&
            centroid[1] - standardDeviation[1] >= -180.0 &&
            centroid[1] - standardDeviation[1] < 180.0
        ) {
            centroid[1] -= standardDeviation[1]
        }
        return centroid
    }
}